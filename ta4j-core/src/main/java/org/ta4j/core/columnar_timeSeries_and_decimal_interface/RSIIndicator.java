/*
  The MIT License (MIT)

  Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)

  Permission is hereby granted, free of charge, to any person obtaining a copy of
  this software and associated documentation files (the "Software"), to deal in
  the Software without restriction, including without limitation the rights to
  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  the Software, and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.columnar_timeSeries_and_decimal_interface;


import java.math.BigDecimal;

/**
 * Relative strength index indicator.
 * <p></p>
 * This calculation of RSI uses traditional moving averages
 * as opposed to Wilder's accumulative moving average technique.
 *
 * @see <a href="https://www.barchart.com/education/technical-indicators#/studies/std_rsi_mod">
 * RSI calculation</a>.
 *
 */
public class RSIIndicator extends CachedIndicator<Value> {

    private final Indicator<Value> averageGainIndicator;
    private final Indicator<Value> averageLossIndicator;
    private final NumOperationsFactory<Value> numFa = getNumFactory();
    
    public RSIIndicator(Indicator<Value> indicator, int timeFrame) {
        this(new AverageGainIndicator(indicator, timeFrame),
                new AverageLossIndicator(indicator, timeFrame));
    }

    public RSIIndicator(Indicator<Value> avgGainIndicator, Indicator<Value> avgLossIndicator) {
        super(avgGainIndicator);
        averageGainIndicator = avgGainIndicator;
        averageLossIndicator = avgLossIndicator;
    }

    @Override
    protected Value calculate(int index) {
        if (index == 0) {
            return numFa.valueOf(0);
        }

        // Relative strength
        Value averageLoss = averageLossIndicator.getValue(index);
        if (averageLoss.isZero()) {
            return numFa.valueOf(100);
        }
        Value averageGain = averageGainIndicator.getValue(index);
        Value relativeStrength = averageGain.dividedBy(averageLoss);

        // Nominal case
        Value ratio = numFa.valueOf(100).dividedBy(numFa.valueOf(1).plus(relativeStrength));
        return numFa.valueOf(100).minus(ratio);
    }

}
