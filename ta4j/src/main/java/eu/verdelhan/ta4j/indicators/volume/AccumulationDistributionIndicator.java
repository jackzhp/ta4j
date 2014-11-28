/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Marc de Verdelhan & respective authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.indicators.volume;


import eu.verdelhan.ta4j.TADecimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 * Accumulation-distribution indicator.
 * <p>
 */
public class AccumulationDistributionIndicator extends CachedIndicator<TADecimal> {

    private TimeSeries series;

    public AccumulationDistributionIndicator(TimeSeries series) {
        setTimeSeries(series);
        this.series = series;
    }

    @Override
    protected TADecimal calculate(int index) {
        if (index == 0) {
            return TADecimal.ZERO;
        }
        Tick tick = series.getTick(index);

        // Calculating the money flow multiplier
        TADecimal moneyFlowMultiplier = ((tick.getClosePrice().minus(tick.getMinPrice())).minus(tick.getMaxPrice().minus(tick.getClosePrice())))
                 .dividedBy(tick.getMaxPrice().minus(tick.getMinPrice()));

        // Calculating the money flow volume
        TADecimal moneyFlowVolume = moneyFlowMultiplier.multipliedBy(tick.getVolume());

        return moneyFlowVolume.plus(getValue(index - 1));
    }
}
