/*
 * Copyright © 2013 VillageReach.  All Rights Reserved.  This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.core.domain;

import lombok.Getter;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openlmis.core.serializer.RightDeSerializer;
import org.openlmis.core.serializer.RightSerializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;

@JsonSerialize(using = RightSerializer.class)
@JsonDeserialize(using = RightDeSerializer.class)
public enum Right {

  CONFIGURE_RNR("right.configure.rnr", TRUE, "Permission to create and edit r&r template for any program", 1),
  MANAGE_FACILITY("right.manage.facility", TRUE, "Permission to manage facility(crud)", 2),
  MANAGE_ROLE("right.manage.role", TRUE, "Permission to create and edit roles in the system", 5),
  MANAGE_SCHEDULE("right.manage.schedule", TRUE, "Permission to create and edit schedules in the system", 6),
  MANAGE_USERS("right.manage.user", TRUE, "Permission to manage users(crud)", 7),
  UPLOADS("right.upload", TRUE, "Permission to upload", 8),
  VIEW_REPORTS("right.view.report", TRUE, "Permission to view reports", 11),
  MANAGE_REPORTS("right.manage.report", TRUE, "Permission to manage reports", 10, VIEW_REPORTS),
  VIEW_REQUISITION("right.view.requisition", FALSE, "Permission to view requisitions", 16),
  CREATE_REQUISITION("right.create.requisition", FALSE, "Permission to create, edit, submit and recall requisitions", 15, VIEW_REQUISITION),
  AUTHORIZE_REQUISITION("right.authorize.requisition", FALSE, "Permission to edit, authorize and recall requisitions", 13, VIEW_REQUISITION),
  APPROVE_REQUISITION("right.approve.requisition", FALSE, "Permission to approve requisitions", 12, VIEW_REQUISITION),
  CONVERT_TO_ORDER("right.convert.to.order", TRUE, "Permission to convert requisitions to order", 14),
  VIEW_ORDER("right.view.order", TRUE, "Permission to view orders", 17),
  MANAGE_PROGRAM_PRODUCT("right.manage.program.product", TRUE, "Permission to manage program products", 3),
  MANAGE_DISTRIBUTION("right.manage.distribution", FALSE, "Permission to manage an distribution", 9),
  MANAGE_REGIMEN_TEMPLATE("right.manage.regimen.template", TRUE, "Permission to manage a regimen template", 4);

  @Getter
  private final String rightName;

  @Getter
  private Boolean adminRight;

  @Getter
  private final String description;

  @Getter
  private List<Right> defaultRights;

  @Getter
  private final Integer displayOrder;

  private Right(String rightName, Boolean adminRight, String description, Integer displayOrder) {
    this(rightName, adminRight, description, displayOrder, new Right[0]);
  }

  private Right(String rightName, Boolean adminRight, String description, Integer displayOrder, Right... rights) {
    this.rightName = rightName;
    this.adminRight = adminRight;
    this.description = description;
    this.displayOrder = displayOrder;
    this.defaultRights = asList(rights);
  }

  public static String commaSeparateRightNames(Right... rights) {
    List<String> rightNames = new ArrayList<>();
    for (Right right : rights) {
      rightNames.add(right.name());
    }
    return rightNames.toString().replace("[", "{").replace("]", "}");
  }

  public static class RightComparator implements Comparator<Right> {
    @Override
    public int compare(Right right1, Right right2) {
      if (right1 == right2) return 0;
      if (right1 == null) {
        return 1;
      }
      if (right2 == null) {
        return -1;
      }
      return right1.getDisplayOrder().compareTo(right2.getDisplayOrder());
    }
  }
}
