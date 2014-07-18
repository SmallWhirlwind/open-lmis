/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */

package org.openlmis.report.controller;

import lombok.NoArgsConstructor;
import org.openlmis.report.Report;
import org.openlmis.report.ReportManager;
import org.openlmis.report.model.Pages;
import org.openlmis.report.model.report.*;
import org.openlmis.report.response.OpenLmisResponse;
import org.openlmis.report.service.LabEquipmentStatusReportDataProvider;
import org.openlmis.report.service.LabEquipmentsByDonorReportDataProvider;
import org.openlmis.report.service.PushedProductReportDataProvider;
import org.openlmis.report.service.UserSummaryReportProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/reports")
public class InteractiveReportController extends BaseController {

  @Autowired
  public ReportManager reportManager;


  @RequestMapping(value = "/reportdata/facilitylist", method = GET, headers = BaseController.ACCEPT_JSON)
  public Pages getFacilityLists(
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
    HttpServletRequest request
  ) {

    Report report = reportManager.getReportByKey("facilities");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<FacilityReport> facilityReportList = (List<FacilityReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, facilityReportList);
  }

  @RequestMapping(value = "/reportdata/mailingLabels", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_MAILING_LABEL_REPORT')")
  public Pages getFacilityListsWtihLables( //@PathVariable(value = "reportKey") String reportKey,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                           HttpServletRequest request
  ) {

    Report report = reportManager.getReportByKey("mailinglabels");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<MailingLabelReport> mailingLabelReports = (List<MailingLabelReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
    return new Pages(page, max, mailingLabelReports);
  }

  @RequestMapping(value = "/reportdata/consumption", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_CONSUMPTION_REPORT')")
  public Pages getConsumptionData(
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
    HttpServletRequest request

  ) {


    Report report = reportManager.getReportByKey("consumption");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<ConsumptionReport> consumptionReportList =
      (List<ConsumptionReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
    return new Pages(page, max, consumptionReportList);
  }

  @RequestMapping(value = "/reportdata/averageConsumption", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_AVERAGE_CONSUMPTION_REPORT')")
  public Pages getAverageConsumptionData(
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
    HttpServletRequest request

  ) {

    Report report = reportManager.getReportByKey("average_consumption");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<AverageConsumptionReport> averageConsumptionReportList =
      (List<AverageConsumptionReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, averageConsumptionReportList);
  }


  @RequestMapping(value = "/reportdata/summary", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_SUMMARY_REPORT')")
  public Pages getSummaryData(
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
    HttpServletRequest request
  ) {


    Report report = reportManager.getReportByKey("summary");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<SummaryReport> reportList =
      (List<SummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, reportList);
  }

  @RequestMapping(value = "/reportdata/non_reporting", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_NON_REPORTING_FACILITIES')")
  public Pages getNonReportingFacilitiesData( //@PathVariable(value = "reportKey") String reportKey,
                                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                              // @RequestParam(value = "period", required = false, defaultValue = "0") int period ,
                                              HttpServletRequest request
  ) {


    Report report = reportManager.getReportByKey("non_reporting");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<MasterReport> reportList =
      (List<MasterReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, reportList);
  }

  @RequestMapping(value = "/reportdata/adjustmentSummary", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_ADJUSTMENT_SUMMARY_REPORT')")
  public Pages getAdjustmentSummaryData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                        HttpServletRequest request

  ) {

    Report report = reportManager.getReportByKey("adjustment_summary");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<AdjustmentSummaryReport> adjustmentSummaryReportList = (List<AdjustmentSummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, adjustmentSummaryReportList);
  }

  @RequestMapping(value = "/reportdata/districtConsumption", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_DISTRICT_CONSUMPTION_REPORT')")
  public Pages getDistrictConsumptionData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                          HttpServletRequest request

  ) {

    Report report = reportManager.getReportByKey("district_consumption");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<DistrictConsumptionReport> districtConsumptionReportList =
      (List<DistrictConsumptionReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, districtConsumptionReportList);
  }

  @RequestMapping(value = "/reportdata/aggregateConsumption", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_DISTRICT_CONSUMPTION_REPORT')")
  public Pages getAggregateConsumptionData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                          HttpServletRequest request

  ) {

    Report report = reportManager.getReportByKey("aggregate_consumption");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<DistrictConsumptionReport> districtConsumptionReportList =
        (List<DistrictConsumptionReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, districtConsumptionReportList);
  }

  @RequestMapping(value = "/reportdata/viewOrders", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_ORDER_REPORT')")
  public Pages getOrderSummaryData(
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
    HttpServletRequest request

  ) {


    Report report = reportManager.getReportByKey("order_summary");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<OrderSummaryReport> orderReportList =
      (List<OrderSummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, orderReportList);
  }

  @RequestMapping(value = "/reportdata/supply_status", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_SUPPLY_STATUS_REPORT')")
  public Pages getSupplyStatusData(
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
    HttpServletRequest request

  ) {


    Report report = reportManager.getReportByKey("supply_status");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<SupplyStatusReport> supplyStatusReportList =
      (List<SupplyStatusReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, supplyStatusReportList);
  }

  @RequestMapping(value = "/reportdata/stockImbalance", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_STOCK_IMBALANCE_REPORT')")
  public Pages getStockImbalanceData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                     @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                     HttpServletRequest request

  ) {


    Report report = reportManager.getReportByKey("stock_imbalance");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<StockImbalanceReport> stockImbalanceReportList =
      (List<StockImbalanceReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
    return new Pages(page, max, stockImbalanceReportList);
  }


  //
  @RequestMapping(value = "/reportdata/stockedOut", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_STOCKED_OUT_REPORT')")
  public Pages getStockedOutData(
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
    HttpServletRequest request
  ) {


    Report report = reportManager.getReportByKey("stocked_out");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<StockedOutReport> stockedOutReportList =
      (List<StockedOutReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);

    return new Pages(page, max, stockedOutReportList);
  }

  @RequestMapping(value = "/reportdata/rnr_feedback", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_RNR_FEEDBACK_REPORT')")
  public Pages getRnRFeedbackReportData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                        HttpServletRequest request

  ) {


    Report report = reportManager.getReportByKey("rnr_feedback");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<RnRFeedbackReport> rnRFeedbackReports =
      (List<RnRFeedbackReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
    return new Pages(page, max, rnRFeedbackReports);
  }


  @RequestMapping(value = "/reportdata/orderFillRate", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_ORDER_FILL_RATE_REPORT')")
  public Pages getOrderFillRateData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                    @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                    HttpServletRequest request

  ) {


    Report report = reportManager.getReportByKey("order_fill_rate");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<MasterReport> orderFillRateReportList =
      (List<MasterReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
    return new Pages(page, max, orderFillRateReportList);
  }

  @RequestMapping(value = "/reportdata/regimenSummary", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_REGIMEN_SUMMARY_REPORT')")
  public Pages getRegimenSummaryData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                     @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                     HttpServletRequest request

  ) {


    Report report = reportManager.getReportByKey("regimen_summary");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<RegimenSummaryReport> regimenSummaryReportList =
      (List<RegimenSummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
    return new Pages(page, max, regimenSummaryReportList);
  }
    @RequestMapping(value = "/reportdata/aggregateRegimenSummary", method = GET, headers = BaseController.ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_REGIMEN_SUMMARY_REPORT')")
    public Pages getAggregateRegimenSummaryData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                       @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                       HttpServletRequest request

    ) {


        Report report = reportManager.getReportByKey("aggregate_regimen_summary");
        report.getReportDataProvider().setUserId(loggedInUserId(request));
        List<RegimenSummaryReport> regimenSummaryReportList =
                (List<RegimenSummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
        return new Pages(page, max, regimenSummaryReportList);
    }
    @RequestMapping(value = "/reportdata/getRegimenDistribution", method = GET, headers = BaseController.ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_REGIMEN_SUMMARY_REPORT')")
    public Pages getRegimenDistributionData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                                HttpServletRequest request

    ) {


        Report report = reportManager.getReportByKey("regimen_distribution");
        report.getReportDataProvider().setUserId(loggedInUserId(request));
        List<RegimenSummaryReport> regimenSummaryReportList =
                (List<RegimenSummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
        return new Pages(page, max, regimenSummaryReportList);
    }

  @RequestMapping(value = "/reportdata/districtFinancialSummary", method = GET, headers = BaseController.ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_DISTRICT_FINANCIAL_SUMMARY_REPORT')")
  public Pages geDistrictFinancialSummaryData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                              HttpServletRequest request

  ) {
    Report report = reportManager.getReportByKey("district_financial_summary");
    report.getReportDataProvider().setUserId(loggedInUserId(request));
    List<DistrictSummaryReport> districtSummaryReportList =
      (List<DistrictSummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
    return new Pages(page, max, districtSummaryReportList);
  }


    @RequestMapping(value = "/reportdata/userSummary", method = GET, headers = BaseController.ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_USER_SUMMARY_REPORT')")
    public Pages getUserSummaryData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                                HttpServletRequest request

    ) {
        Report report = reportManager.getReportByKey("user_summary");
        report.getReportDataProvider().setUserId(loggedInUserId(request));
        List<UserSummaryReport> userSummmaryReportList =
                (List<UserSummaryReport>) report.getReportDataProvider().getMainReportData(request.getParameterMap(), request.getParameterMap(), page, max);
        return new Pages(page, max, userSummmaryReportList);
    }

    @RequestMapping(value = "/reportdata/userRoleAssignmentSummary", method = GET, headers = BaseController.ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_USER_SUMMARY_REPORT')")
    public ResponseEntity<OpenLmisResponse> getUseRoleAssignmentSummary( HttpServletRequest request

    ) {
        Report report = reportManager.getReportByKey("user_summary");
        report.getReportDataProvider().setUserId(loggedInUserId(request));
        UserSummaryReportProvider provider = (UserSummaryReportProvider) report.getReportDataProvider();
        return OpenLmisResponse.response("userAssignment",provider.getUserAssignments());
    }

    @RequestMapping(value = "/reportdata/labEquipmentList", method = GET, headers = BaseController.ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_LAB_EQUIPMENT_LIST_REPORT')")
    public Pages getLabEquipmentStatus(  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                         @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                         HttpServletRequest request) {

        Report report = reportManager.getReportByKey("lab_equipment_list");
        report.getReportDataProvider().setUserId(loggedInUserId(request));
        LabEquipmentStatusReportDataProvider provider = (LabEquipmentStatusReportDataProvider) report.getReportDataProvider();
        List<LabEquipmentStatusReport> labEquipmentStatusList = (List<LabEquipmentStatusReport>)
                provider.getMainReportData(request.getParameterMap(), request.getParameterMap(),page, max);

        return new Pages(page, max, labEquipmentStatusList);
    }

    @RequestMapping(value = "/reportdata/labEquipmentsByFundingSource", method = GET, headers = BaseController.ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_LAB_EQUIPMENTS_BY_FUNDING_SOURCE')")
    public Pages getLabEquipmentListByFundingSource(  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                         @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                         HttpServletRequest request) {

        Report report = reportManager.getReportByKey("lab_equipments_by_donor");
        report.getReportDataProvider().setUserId(loggedInUserId(request));
        LabEquipmentsByDonorReportDataProvider provider = (LabEquipmentsByDonorReportDataProvider) report.getReportDataProvider();
        List<LabEquipmentsByDonorReport> labEquipmentsListByDonor = (List<LabEquipmentsByDonorReport>)
                provider.getMainReportData(request.getParameterMap(), request.getParameterMap(),page, max);

        return new Pages(page, max, labEquipmentsListByDonor);
    }

    @RequestMapping(value = "/reportdata/pushedProductList", method = GET, headers = BaseController.ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_ORDER_FILL_RATE_REPORT')")
    public Pages getPushedProductsReport(  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "max", required = false, defaultValue = "10") int max,
                                           HttpServletRequest request) {

        Report report = reportManager.getReportByKey("pushed_product_list");
        report.getReportDataProvider().setUserId(loggedInUserId(request));
        PushedProductReportDataProvider provider = (PushedProductReportDataProvider) report.getReportDataProvider();
        List<OrderFillRateReport> pushedProductList = (List<OrderFillRateReport>)
                provider.getMainReportData(request.getParameterMap(), request.getParameterMap(),page, max);

        return new Pages(page, max, pushedProductList);
    }
}