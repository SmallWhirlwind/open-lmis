/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonSerialize(include = NON_NULL)
public class Program extends BaseModel {

  private String code;
  private String name;
  private String description;
  private Boolean active;
  private boolean templateConfigured;
  private boolean regimenTemplateConfigured;
  private boolean push;

  public Program(Long id) {
    this.id = id;
  }

  public Program(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Program(Long id, String code, String name, String description, Boolean active, boolean templateConfigured) {
    this.code = code;
    this.name = name;
    this.description = description;
    this.active = active;
    this.templateConfigured = templateConfigured;
    this.id = id;
  }

  public Program basicInformation() {
    return new Program(id, name);
  }
}
