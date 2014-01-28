/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */

package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.report.mapper.RegimenSummaryReportMapper;
import org.openlmis.report.model.ReportData;
import org.openlmis.report.model.filter.RegimenSummaryReportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@NoArgsConstructor
public class RegimenSummaryReportDataProvider extends ReportDataProvider {

  private RegimenSummaryReportMapper reportMapper;
  private ConfigurationSettingService configurationService;

  @Autowired
  public RegimenSummaryReportDataProvider(RegimenSummaryReportMapper mapper, ConfigurationSettingService configurationService) {
    this.reportMapper = mapper;
    this.configurationService = configurationService;
  }

  @Override
  protected List<? extends ReportData> getResultSetReportData(Map<String, String[]> filterCriteria) {
    RowBounds rowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
    return reportMapper.getReport(getReportFilterData(filterCriteria), null, rowBounds);
  }

  @Override
  public List<? extends ReportData> getReportDataByFilterCriteriaAndPagingAndSorting(Map<String, String[]> filterCriteria, Map<String, String[]> SortCriteria, int page, int pageSize) {
    RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
    return reportMapper.getReport(getReportFilterData(filterCriteria), SortCriteria, rowBounds);
  }

  public ReportData getReportFilterData(Map<String, String[]> filterCriteria) {
    RegimenSummaryReportFilter regimenSummaryReportFilter = null;

    if (filterCriteria != null) {
      regimenSummaryReportFilter = new RegimenSummaryReportFilter();

      regimenSummaryReportFilter.setZoneId(StringUtils.isBlank(filterCriteria.get("zoneId")[0]) ? 0 : Integer.parseInt(filterCriteria.get("zoneId")[0]));  //defaults to 0
      regimenSummaryReportFilter.setZone(StringUtils.isBlank(filterCriteria.get("zone")[0]) ? "All Geographic Zones" : filterCriteria.get("zone")[0]);
      regimenSummaryReportFilter.setRegimen(StringUtils.isBlank(filterCriteria.get("regimen")[0]) ? "All Regimens" : filterCriteria.get("regimen")[0]);
      regimenSummaryReportFilter.setRegimenId(StringUtils.isBlank(filterCriteria.get("regimenId")[0]) ? 0 : Integer.parseInt(filterCriteria.get("regimenId")[0]));  //defaults to 0
      regimenSummaryReportFilter.setGeographicLevelId(StringUtils.isBlank(filterCriteria.get("geographicLevelId")[0]) ? 0 : Integer.parseInt(filterCriteria.get("geographicLevelId")[0]));  //defaults to 0
      regimenSummaryReportFilter.setGeographicLevel(StringUtils.isBlank(filterCriteria.get("geographicLevel")[0]) ? "All Geographic Levels" : filterCriteria.get("geographicLevel")[0]);
      regimenSummaryReportFilter.setRegimenCategoryId(StringUtils.isBlank(filterCriteria.get("regimenCategoryId")[0]) ? 0 : Integer.parseInt(filterCriteria.get("regimenCategoryId")[0]));  //defaults to 0
      regimenSummaryReportFilter.setRegimenCategory(StringUtils.isBlank(filterCriteria.get("regimenCategory")[0]) ? "All Regimen Categories" : filterCriteria.get("regimenCategory")[0]);
      regimenSummaryReportFilter.setRgroup((filterCriteria.get("rgroup") == null || filterCriteria.get("rgroup")[0].equals("")) ? "All Reporting Groups" : filterCriteria.get("rgroup")[0]);

      regimenSummaryReportFilter.setRgroupId(filterCriteria.get("rgroupId") == null ? 0 : Integer.parseInt(filterCriteria.get("rgroupId")[0])); //defaults to 0
      regimenSummaryReportFilter.setProgramId(StringUtils.isBlank(filterCriteria.get("programId")[0]) ? 0 : Integer.parseInt(filterCriteria.get("programId")[0]));  //defaults to 0
      if (regimenSummaryReportFilter.getProgramId() == 0 || regimenSummaryReportFilter.getProgramId() == -1)
        regimenSummaryReportFilter.setProgram("All Programs");
      else
        regimenSummaryReportFilter.setProgram(filterCriteria.get("program")[0]);

      regimenSummaryReportFilter.setScheduleId(StringUtils.isBlank(filterCriteria.get("scheduleId")[0]) ? 0 : Integer.parseInt(filterCriteria.get("scheduleId")[0])); //defaults to 0
      regimenSummaryReportFilter.setSchedule(StringUtils.isBlank(filterCriteria.get("schedule")[0]) ? "All Schedules" : filterCriteria.get("schedule")[0]);
      regimenSummaryReportFilter.setPeriodId(StringUtils.isBlank(filterCriteria.get("periodId")[0]) ? 0 : Integer.parseInt(filterCriteria.get("periodId")[0])); //defaults to 0
      regimenSummaryReportFilter.setPeriod(StringUtils.isBlank(filterCriteria.get("period")[0]) ? "All Periods" : filterCriteria.get("period")[0]);

      regimenSummaryReportFilter.setYear(filterCriteria.get("year") == null ? 0 : Integer.parseInt(filterCriteria.get("year")[0]));

    }
    return regimenSummaryReportFilter;
  }

  @Override
  public String getFilterSummary(Map<String, String[]> params) {
    return getReportFilterData(params).toString();
  }


}
