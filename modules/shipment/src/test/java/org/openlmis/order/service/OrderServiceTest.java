/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.order.service;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openlmis.core.domain.OrderConfiguration;
import org.openlmis.core.domain.Program;
import org.openlmis.core.domain.SupervisoryNode;
import org.openlmis.core.domain.SupplyLine;
import org.openlmis.core.repository.OrderConfigurationRepository;
import org.openlmis.core.service.SupplyLineService;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.order.domain.DateFormat;
import org.openlmis.order.domain.Order;
import org.openlmis.order.domain.OrderFileColumn;
import org.openlmis.order.domain.OrderStatus;
import org.openlmis.order.dto.OrderFileTemplateDTO;
import org.openlmis.order.repository.OrderRepository;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.domain.RnrLineItem;
import org.openlmis.rnr.service.RequisitionService;
import org.openlmis.shipment.domain.ShipmentFileInfo;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openlmis.order.domain.DateFormat.*;
import static org.openlmis.order.domain.OrderStatus.*;
import static org.openlmis.rnr.builder.RequisitionBuilder.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@Category(UnitTests.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(OrderService.class)
public class OrderServiceTest {

  @Mock
  private OrderConfigurationRepository orderConfigurationRepository;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private RequisitionService requisitionService;

  @Mock
  private SupplyLineService supplyLineService;

  @InjectMocks
  private OrderService orderService;

  @Test
  public void shouldSaveOrder() throws Exception {
    Order order = new Order();
    orderService.save(order);
    verify(orderRepository).save(order);
  }

  @Test
  public void shouldConvertRequisitionsToOrderWithStatusInRoute() throws Exception {
    Program program = new Program();
    Long userId = 1L;
    Rnr rnr = new Rnr();
    rnr.setId(1L);
    rnr.setSupervisoryNodeId(1L);
    rnr.setProgram(program);

    SupplyLine supplyLine = new SupplyLine();
    supplyLine.setExportOrders(Boolean.TRUE);

    when(requisitionService.getLWById(1L)).thenReturn(rnr);
    SupervisoryNode supervisoryNode = new SupervisoryNode(1L);
    whenNew(SupervisoryNode.class).withArguments(1l).thenReturn(supervisoryNode);
    when(supplyLineService.getSupplyLineBy(supervisoryNode, program)).thenReturn(supplyLine);

    List<Rnr> rnrList = new ArrayList<>();
    rnrList.add(rnr);

    orderService.convertToOrder(rnrList, userId);

    Order order = new Order(rnr);
    order.setStatus(OrderStatus.IN_ROUTE);
    order.setSupplyLine(supplyLine);
    verify(orderRepository).save(order);
    verify(supplyLineService).getSupplyLineBy(supervisoryNode, program);
    verify(requisitionService).getLWById(rnr.getId());
    verify(requisitionService).releaseRequisitionsAsOrder(rnrList, userId);
    assertThat(order.getSupplyLine(), is(supplyLine));
  }

  @Test
  public void shouldConvertRequisitionsToOrderWithStatusReadyToPack() throws Exception {
    Program program = new Program();
    Long userId = 1L;
    Rnr rnr = new Rnr();
    rnr.setId(1L);
    rnr.setSupervisoryNodeId(1L);
    rnr.setProgram(program);

    SupplyLine supplyLine = new SupplyLine();
    supplyLine.setExportOrders(Boolean.FALSE);

    when(requisitionService.getLWById(1L)).thenReturn(rnr);
    SupervisoryNode supervisoryNode = new SupervisoryNode(1L);
    whenNew(SupervisoryNode.class).withArguments(1l).thenReturn(supervisoryNode);
    when(supplyLineService.getSupplyLineBy(supervisoryNode, program)).thenReturn(supplyLine);

    List<Rnr> rnrList = new ArrayList<>();
    rnrList.add(rnr);

    orderService.convertToOrder(rnrList, userId);

    Order order = new Order(rnr);
    order.setStatus(OrderStatus.READY_TO_PACK);
    order.setSupplyLine(supplyLine);
    verify(orderRepository).save(order);
    verify(supplyLineService).getSupplyLineBy(supervisoryNode, program);
    verify(requisitionService).getLWById(rnr.getId());
    verify(requisitionService).releaseRequisitionsAsOrder(rnrList, userId);
    assertThat(order.getSupplyLine(), is(supplyLine));
  }

  @Test
  public void shouldConvertRequisitionsToOrderWithStatusTransferFailed() throws Exception {
    String SUPPLY_LINE_MISSING_COMMENT = "order.ftpComment.supplyline.missing";
    Program program = new Program();
    Long userId = 1L;
    Rnr rnr = new Rnr();
    rnr.setId(1L);
    rnr.setSupervisoryNodeId(1L);
    rnr.setProgram(program);

    when(requisitionService.getLWById(1L)).thenReturn(rnr);
    SupervisoryNode supervisoryNode = new SupervisoryNode(1L);
    whenNew(SupervisoryNode.class).withArguments(1l).thenReturn(supervisoryNode);
    when(supplyLineService.getSupplyLineBy(supervisoryNode, program)).thenReturn(null);

    List<Rnr> rnrList = new ArrayList<>();
    rnrList.add(rnr);

    orderService.convertToOrder(rnrList, userId);

    Order order = new Order(rnr);
    order.setStatus(OrderStatus.TRANSFER_FAILED);
    order.setFtpComment(SUPPLY_LINE_MISSING_COMMENT);
    order.setSupplyLine(null);
    verify(orderRepository).save(order);
    verify(supplyLineService).getSupplyLineBy(supervisoryNode, program);
    verify(requisitionService).getLWById(rnr.getId());
    verify(requisitionService).releaseRequisitionsAsOrder(rnrList, userId);
  }

  @Test
  public void shouldGetOrdersFilledWithRequisition() throws Exception {
    Rnr rnr1 = make(a(defaultRnr, with(id, 78L)));
    final Order order1 = new Order();
    order1.setRnr(rnr1);

    Rnr rnr2 = make(a(defaultRnr, with(periodId, 2L), with(id, 72L)));

    final Order order2 = new Order();
    order2.setRnr(rnr2);

    List<Order> expectedOrders = new ArrayList<Order>() {{
      add(order1);
      add(order2);
    }};

    when(orderRepository.getOrders()).thenReturn(expectedOrders);
    when(requisitionService.getFullRequisitionById(rnr1.getId())).thenReturn(rnr1);
    when(requisitionService.getFullRequisitionById(rnr2.getId())).thenReturn(rnr2);

    List<Order> orders = orderService.getOrders();
    assertThat(orders, is(expectedOrders));
    verify(orderRepository).getOrders();
    verify(requisitionService).getFullRequisitionById(rnr1.getId());
    verify(requisitionService).getFullRequisitionById(rnr2.getId());
  }

  @Test
  public void shouldSetReleasedForAllOrdersIfErrorInShipment() throws Exception {
    Set<Long> orderIds = new HashSet<>();
    orderIds.add(123L);
    orderIds.add(456L);
    long shipmentId = 678L;

    boolean processingError = true;

    ShipmentFileInfo shipmentFileInfo = new ShipmentFileInfo("shipmentFile.csv", processingError);
    shipmentFileInfo.setId(shipmentId);

    orderService.updateStatusAndShipmentIdForOrders(orderIds, shipmentFileInfo);

    verify(orderRepository).updateStatusAndShipmentIdForOrder(123L, RELEASED, shipmentId);
    verify(orderRepository).updateStatusAndShipmentIdForOrder(456L, RELEASED, shipmentId);
  }

  @Test
  public void shouldSetPackedStatusForAllOrdersIfNoErrorInShipment() throws Exception {
    Set<Long> orderIds = new HashSet<>();
    orderIds.add(123L);
    orderIds.add(456L);
    long shipmentId = 678L;

    boolean processingError = false;

    ShipmentFileInfo shipmentFileInfo = new ShipmentFileInfo("shipmentFile.csv", processingError);
    shipmentFileInfo.setId(shipmentId);

    orderService.updateStatusAndShipmentIdForOrders(orderIds, shipmentFileInfo);

    verify(orderRepository).updateStatusAndShipmentIdForOrder(123L, PACKED, shipmentId);
    verify(orderRepository).updateStatusAndShipmentIdForOrder(456L, PACKED, shipmentId);
  }

  @Test
  public void shouldGetOrderWithoutUnorderedProducts() {
    Long orderId = 1L;
    Long rnrId = 1L;
    Order order = new Order();

    Rnr rnr = new Rnr();
    rnr.setId(rnrId);
    RnrLineItem rnrLineItem = new RnrLineItem();
    List<RnrLineItem> lineItems = new ArrayList<>();
    rnrLineItem.setPacksToShip(0);
    lineItems.add(rnrLineItem);
    rnr.setFullSupplyLineItems(lineItems);
    rnr.setNonFullSupplyLineItems(lineItems);
    order.setRnr(rnr);

    when(orderRepository.getById(orderId)).thenReturn(order);
    when(requisitionService.getFullRequisitionById(rnr.getId())).thenReturn(rnr);

    Order expectedOrder = orderService.getOrderForDownload(orderId);

    verify(orderRepository).getById(orderId);
    verify(requisitionService).getFullRequisitionById(rnr.getId());
    assertThat(order.getRnr().getFullSupplyLineItems().size(), is(0));
    assertThat(order.getRnr().getNonFullSupplyLineItems().size(), is(0));
    assertThat(expectedOrder, is(order));
  }

  @Test
  public void shouldGetOrderFileTemplateWithConfiguration() throws Exception {
    OrderConfiguration orderConfiguration = new OrderConfiguration();
    List<OrderFileColumn> orderFileColumns = new ArrayList<>();
    OrderFileTemplateDTO expectedOrderFileTemplateDTO = new OrderFileTemplateDTO(orderConfiguration, orderFileColumns);
    when(orderConfigurationRepository.getConfiguration()).thenReturn(orderConfiguration);
    when(orderRepository.getOrderFileTemplate()).thenReturn(orderFileColumns);
    OrderFileTemplateDTO actualOrderFileTemplateDTO = orderService.getOrderFileTemplateDTO();
    verify(orderConfigurationRepository).getConfiguration();
    verify(orderRepository).getOrderFileTemplate();
    assertThat(actualOrderFileTemplateDTO, is(expectedOrderFileTemplateDTO));
  }

  @Test
  public void shouldSaveOrderFileColumnsWithConfiguration() throws Exception {
    OrderConfiguration orderConfiguration = new OrderConfiguration();
    List<OrderFileColumn> orderFileColumns = new ArrayList<>();
    OrderFileTemplateDTO orderFileTemplateDTO = new OrderFileTemplateDTO(orderConfiguration, orderFileColumns);
    Long userId = 1L;
    orderService.saveOrderFileTemplate(orderFileTemplateDTO, userId);
    verify(orderConfigurationRepository).update(orderConfiguration);
    verify(orderRepository).saveOrderFileColumns(orderFileColumns, userId);
  }

  @Test
  public void shouldGetAllDateFormats() throws Exception {
    List<DateFormat> dateFormats = new ArrayList<>(orderService.getAllDateFormats());
    List<DateFormat> expectedDateFormats = asList(DATE_1, DATE_2, DATE_3, DATE_4, DATE_5, DATE_6, DATE_7, DATE_8, DATE_9, DATE_10,
      DATE_11, DATE_12, DATE_13, DATE_14, DATE_15, DATE_16, DATE_17, DATE_18, DATE_19, DATE_20,
      DATE_21, DATE_22, DATE_23, DATE_24, DATE_25, DATE_26, DATE_27, DATE_28, DATE_29, DATE_30
    );

    assertThat(dateFormats, is(expectedDateFormats));
  }

  @Test
  public void shouldUpdateOrderStatusAndFtpComment() throws Exception {
    Order order = new Order();
    orderService.updateOrderStatus(order);
    verify(orderRepository).updateOrderStatus(order);
  }

  @Test
  public void shouldReturnTrueIfOrderIsReleased() throws Exception {
    long orderId = 123L;
    when(orderRepository.getStatus(orderId)).thenReturn(RELEASED);

    assertThat(orderService.isShippable(orderId), is(true));

    verify(orderRepository).getStatus(123L);

  }



  @Test
  public void shouldReturnTrueIfOrderIsNotShippable() throws Exception {
    long orderId = 123L;
    when(orderRepository.getStatus(orderId))
      .thenReturn(IN_ROUTE)
      .thenReturn(PACKED)
      .thenReturn(TRANSFER_FAILED)
      .thenReturn(READY_TO_PACK);

    assertThat(orderService.isShippable(orderId), is(false));
    assertThat(orderService.isShippable(orderId), is(false));
    assertThat(orderService.isShippable(orderId), is(false));
    assertThat(orderService.isShippable(orderId), is(false));

    verify(orderRepository, times(4)).getStatus(123L);

  }
}
