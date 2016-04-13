function StockOutAllProductsReportController($scope, $filter, $controller, $http, CubesGenerateUrlService, messageService) {
    $controller('BaseProductReportController', {$scope: $scope});

    $scope.getTimeRange =function(dateRange){
        $scope.reportParams.startTime = dateRange.startTime;
        $scope.reportParams.endTime = dateRange.endTime;
    };

    $scope.$on('$viewContentLoaded', function () {
        $scope.loadProducts();
        $scope.loadHealthFacilities();
    });

    $scope.loadReport = function () {
        if ($scope.checkDateValidRange()) {
            generateReportTitle();
            getStockOutDataFromCubes();
        }
    };

    function getStockOutDataFromCubes() {
        $http.get(CubesGenerateUrlService.generateAggregateUrl('vw_stockouts', ["drug", "overlapped_month"], generateCutParams(getStockReportRequestParam())))
            .success(function (data) {
                $scope.reportData = [];
                generateStockOutAverageReportData(data.cells);
                formatReportWhenSelectAllFacility();
        });
    }

    function generateStockOutAverageReportData(data) {
        _.forEach(_.groupBy(data, "drug.drug_code"), function (drug) {
            var sumAvg = 0;
            var sumDuration = 0;
            var totalOccurrences = 0;
            _.forEach(drug, function (stockOut) {
                sumAvg += stockOut.average_days;
                sumDuration += stockOut.overlap_duration;
                totalOccurrences += stockOut.record_count;
            });
            var monthlyAvg = sumAvg / drug.length / getNumOfSelectedFacilities();
            var monthlyOccurrences = totalOccurrences / drug.length / getNumOfSelectedFacilities();
            generateReportItem(drug, sumDuration, monthlyAvg, monthlyOccurrences);
        });
    }

    function generateReportItem(drug, sumDuration, monthlyAvg, monthlyOccurrences) {
        drug.code = drug[0]['drug.drug_code'];
        drug.name = drug[0]['drug.drug_name'];
        drug.totalDuration = sumDuration;
        drug.avgDuration = monthlyAvg;
        drug.occurrences = monthlyOccurrences;
        $scope.reportData.push(drug);
    }

    function getNumOfSelectedFacilities() {
        return $scope.reportParams.facilityId ? 1 : FacilityFilter()($scope.facilities, $scope.districts, $scope.reportParams.districtId, $scope.reportParams.provinceId).length;
    }

    function formatReportWhenSelectAllFacility() {
        if (!$scope.reportParams.facilityId) {
            $scope.reportData.map(function (data) {
                data.totalDuration = "-";
            });
            $scope.occurrencesHeader = messageService.get("report.avg.stock.out.occurrences");
        } else {
            $scope.occurrencesHeader = messageService.get("report.stock.out.occurrences");
        }
    }

    function getStockReportRequestParam() {
        var params = {};
        params.startTime = $filter('date')($scope.reportParams.startTime, "yyyy,MM,dd");
        params.endTime = $filter('date')($scope.reportParams.endTime, "yyyy,MM,dd");
        params.selectedProvince = $scope.getGeographicZoneById($scope.provinces, $scope.reportParams.provinceId);
        params.selectedDistrict = $scope.getGeographicZoneById($scope.districts, $scope.reportParams.districtId);
        params.selectedFacility = _.find($scope.facilities, function (facility) {
            return facility.id == $scope.reportParams.facilityId;
        });
        return params;
    }

    $scope.getGeographicZoneById = function (zones, zoneId) {
        return _.find(zones, function (zone) {
            return zone.id == zoneId;
        });
    };

    function generateCutParams(stockReportParams) {
        var cutsParams = [{
            dimension: "overlapped_date",
            values: [stockReportParams.startTime + "-" + stockReportParams.endTime]
        }];
        if (stockReportParams.selectedFacility) {
            cutsParams.push({dimension: "facility", values: [stockReportParams.selectedFacility.code]});
        }

        if (stockReportParams.selectedProvince && stockReportParams.selectedDistrict) {
            cutsParams.push({
                dimension: "location",
                values: [[stockReportParams.selectedProvince.code, stockReportParams.selectedDistrict.code]]
            });
        } else if (stockReportParams.selectedProvince && !stockReportParams.selectedDistrict) {
            cutsParams.push({dimension: "location", values: [stockReportParams.selectedProvince.code]});
        }
        return cutsParams;
    }

    function generateReportTitle() {
        var stockReportParams = getStockReportRequestParam();
        var reportTitle = "";
        if (stockReportParams.selectedProvince) {
            reportTitle = stockReportParams.selectedProvince.name;
        }
        if (stockReportParams.selectedDistrict) {
            reportTitle += ("," + stockReportParams.selectedDistrict.name);
        }
        if (stockReportParams.selectedFacility) {
            reportTitle += reportTitle === "" ? stockReportParams.selectedFacility.name : ("," + stockReportParams.selectedFacility.name);
        }
        $scope.reportParams.reportTitle = reportTitle || messageService.get("label.all");
    }
}