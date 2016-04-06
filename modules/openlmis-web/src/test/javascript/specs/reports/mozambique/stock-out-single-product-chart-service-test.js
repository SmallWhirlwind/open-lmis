describe("stock out single product chart service test", function () {

    var stockoutSingleProductChartService;


    beforeEach(module('openlmis'));

    beforeEach(function () {
        inject(function (StockoutSingleProductChartService) {
            stockoutSingleProductChartService = StockoutSingleProductChartService;
        })
    });

    it("should generate chart data items", function () {

        var facilityData = {
            code: 'HF'
        };

        var stockoutEvents = [{
            'facility.facility_code': 'HF',
            'stockout.date': '2016-01-01',
            'stockout.resolved_date': '2016-03-01'
        }];
        var chartDataItems = stockoutSingleProductChartService.generateChartDataItems(new Date("2016-03-01"), new Date("2016-03-02"), facilityData, stockoutEvents);

        expect(chartDataItems).toEqual([
            {
                date: new Date("2016-03-01"),
                stockOutBarHeight: 1
            }, {
                date: new Date("2016-03-02"),
                stockOutBarHeight: 0
            }]);
    });

});