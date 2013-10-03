--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--

INSERT INTO master_rnr_columns
(   name                           ,  position  ,                         label                                  , source         , sourceConfigurable         , formula                                                  , indicator                                           , used       , visible       , mandatory    , description)
VALUES
(  'productCode'                   ,          1 , 'Product Code'                                                 ,'R'             , false                      , ''                                                       , 'indicator.column.product.code'                     , true       , true          , false        , 'description.column.product.code'                        ),
(  'product'                       ,          2 , 'Product'                                                      ,'R'             , false                      , ''                                                       , 'indicator.column.product'                          , true       , true          , true         , 'description.column.product'                             ),
(  'dispensingUnit'                ,          3 , 'Unit/Unit of Issue'                                           ,'R'             , false                      , ''                                                       , 'indicator.column.dispensing.unit'                  , true       , true          , false        , 'description.column.dispensing.unit'                     ),
(  'beginningBalance'              ,          4 , 'Beginning Balance'                                            ,'U'             , false                      , ''                                                       , 'indicator.column.beginning.balance'                , true       , true          , false        , 'description.column.beginning.balance'                   ),
(  'quantityReceived'              ,          5 , 'Total Received Quantity'                                      ,'U'             , false                      , ''                                                       , 'indicator.column.quantity.received'                , true       , true          , false        , 'description.column.quantity.received'                   ),
(  'total'                         ,          6 , 'Total'                                                        ,'C'             , false                      , 'formula.column.total'                                   , 'indicator.column.total'                            , true       , true          , false        , 'description.column.total'                               ),
(  'quantityDispensed'             ,          7 , 'Total Consumed Quantity'                                      ,'U'             , true                       , 'formula.column.quantity.dispensed'                      , 'indicator.column.quantity.dispensed'               , true       , true          , false        , 'description.column.quantity.dispensed'                  ),
(  'lossesAndAdjustments'          ,          8 , 'Total Losses / Adjustments'                                   ,'U'             , false                      , 'formula.column.losses.adjustments'                      , 'indicator.column.losses.adjustments'               , true       , true          , false        , 'description.column.losses.adjustments'                  ),
(  'stockInHand'                   ,          9 , 'Stock on Hand'                                                ,'U'             , true                       , 'formula.column.stock.in.hand'                           , 'indicator.column.stock.in.hand'                    , true       , true          , false        , 'description.column.stock.in.hand'                       ),
(  'newPatientCount'               ,         10 , 'Total number of new patients added to service on the program' ,'U'             , false                      , ''                                                       , 'indicator.column.new.patient.count'                , true       , true          , false        , 'description.column.new.patient.count'                   ),
(  'stockOutDays'                  ,         11 , 'Total Stockout Days'                                          ,'U'             , false                      , ''                                                       , 'indicator.column.stock.out.days'                   , true       , true          , false        , 'description.column.stock.out.days'                      ),
(  'normalizedConsumption'         ,         12 , 'Adjusted Total Consumption'                                   ,'C'             , false                      , 'formula.column.normalised.consumption'                  , 'indicator.column.normalized.consumption'           , true       , true          , false        , 'description.column.normalized.consumption'              ),
(  'amc'                           ,         13 , 'Average Monthly Consumption(AMC)'                             ,'C'             , false                      , 'formula.column.amc'                                     , 'indicator.column.amc'                              , true       , true          , false        , 'description.column.amc'                                 ),
(  'maxStockQuantity'              ,         14 , 'Maximum Stock Quantity'                                       ,'C'             , false                      , 'formula.column.max.stock.quantity'                      , 'indicator.column.max.stock.quantity'               , true       , true          , false        , 'description.column.max.stock.quantity'                  ),
(  'calculatedOrderQuantity'       ,         15 , 'Calculated Order Quantity'                                    ,'C'             , false                      , 'formula.column.calculated.order.quantity'               , 'indicator.column.calculated.order.quantity'        , true       , true          , false        , 'description.column.calculated.order.quantity'           ),
(  'quantityRequested'             ,         16 , 'Requested Quantity'                                           ,'U'             , false                      , ''                                                       , 'indicator.column.quantity.requested'               , true       , true          , false        , 'description.column.quantity.requested'                  ),
(  'reasonForRequestedQuantity'    ,         17 , 'Requested Quantity Explanation'                               ,'U'             , false                      , ''                                                       , 'indicator.column.reason.for.requested.quantity'    , true       , true          , false        , 'description.column.reason.for.requested.quantity'       ),
(  'quantityApproved'              ,         18 , 'Approved Quantity'                                            ,'U'             , false                      , ''                                                       , 'indicator.column.quantity.approved'                , true       , true          , false        , 'description.column.quantity.approved'                   ),
(  'packsToShip'                   ,         19 , 'Packs to Ship'                                                ,'C'             , false                      , 'formula.column.packs.to.ship'                           , 'indicator.column.packs.to.ship'                    , true       , true          , false        , 'description.column.packs.to.ship'                       ),
(  'price'                         ,         20 , 'Price per Pack'                                               ,'R'             , false                      , ''                                                       , 'indicator.column.price'                            , true       , true          , false        , 'description.column.price'                               ),
(  'cost'                          ,         21 , 'Total Cost'                                                   ,'C'             , false                      , 'formula.column.cost'                                    , 'indicator.column.cost'                             , true       , true          , false        , 'description.column.cost'                                ),
(  'expirationDate'                ,         22 , 'Expiration Date'                                              ,'U'             , false                      , ''                                                       , 'indicator.column.expiration.date'                  , true       , true          , false        , 'description.column.expiration.date'                     ),
(  'remarks'                       ,         23 , 'Remarks'                                                      ,'U'             , false                      , ''                                                       , 'indicator.column.remarks'                          , true       , true          , false        , 'description.column.remarks'                             );


