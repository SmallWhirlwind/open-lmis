/*
 * Copyright © 2013 VillageReach.  All Rights Reserved.  This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.core.upload;

import org.junit.Test;
import org.openlmis.core.builder.FacilityBuilder;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.repository.FacilityRepository;
import org.openlmis.upload.model.AuditFields;

import java.util.Date;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FacilityPersistenceHandlerTest {

  @Test
  public void shouldSaveFacility() {
    FacilityRepository facilityRepository = mock(FacilityRepository.class);
    FacilityPersistenceHandler facilityPersistenceHandler = new FacilityPersistenceHandler(facilityRepository);
    Facility facility = make(a(FacilityBuilder.defaultFacility));
    Date currentTimestamp = new Date();
    facilityPersistenceHandler.save(facility, new AuditFields(1, currentTimestamp));
    assertThat(facility.getModifiedBy(), is(1));
    assertThat(facility.getModifiedDate(), is(currentTimestamp));
    verify(facilityRepository).save(facility);
  }

}
