services.factory('CubesGenerateUrlService', function () {
    var baseUrl = "/cubesreports/cube/";

    var generateAggregateUrl = function (cubesName, drillDowns, cuts) {
        return baseUrl + cubesName + "/aggregate" + "?drilldown=" + drillDowns.join("|") + "&cut=" + generateCuts(cuts);
    };

    var generateFactsUrl = function (cubesName, cuts) {
        return baseUrl + cubesName + "/facts" + "?cut=" + generateCuts(cuts);
    };

    function generateCuts(cuts) {
        return _.map(cuts, function (cut) {
            return cut.dimension + ":" + cut.values.join(";");
        }).join("|");
    }

    return {
        generateAggregateUrl: generateAggregateUrl,
        generateFactsUrl: generateFactsUrl
    };
});