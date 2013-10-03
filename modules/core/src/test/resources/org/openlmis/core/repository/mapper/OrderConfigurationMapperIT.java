/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.core.repository.mapper;


import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openlmis.core.domain.OrderConfiguration;
import org.openlmis.db.categories.IntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@Category(IntegrationTests.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-applicationContext-core.xml")
@TransactionConfiguration(defaultRollback = true, transactionManager = "openLmisTransactionManager")
@Transactional
public class OrderConfigurationMapperIT {
  @Autowired
  private OrderConfigurationMapper mapper;

  @Test
  public void shouldGetConfiguration() throws Exception {
    OrderConfiguration orderConfiguration = mapper.get();
    assertThat(orderConfiguration.getFilePrefix(), is("O"));
    assertThat(orderConfiguration.getHeaderInFile(), is(false));
  }

  @Test
  public void shouldUpdateConfiguration() throws Exception {
    OrderConfiguration orderConfiguration = new OrderConfiguration();
    orderConfiguration.setHeaderInFile(true);
    orderConfiguration.setFilePrefix("ORD");
    mapper.update(orderConfiguration);
    OrderConfiguration returnedOrderConfiguration = mapper.get();
    assertThat(returnedOrderConfiguration.getHeaderInFile(), is(true));
    assertThat(returnedOrderConfiguration.getFilePrefix(), is("ORD"));
  }
}
