package one.model;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 16:54
 * @Description
 */
public class NumberListModel extends AbstractListModel<BigDecimal>{

    BigDecimal start;
    BigDecimal end;
    BigDecimal step;

    public NumberListModel(BigDecimal start, BigDecimal end, BigDecimal step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public int getSize() {
        int floor = (int) Math.floor(end.subtract(start).divide(step, 2, RoundingMode.HALF_DOWN).doubleValue());
        return floor + 1;
    }

    @Override
    public BigDecimal getElementAt(int index) {
        BigDecimal ele = new BigDecimal(index).multiply(step).add(start).setScale(2, RoundingMode.HALF_DOWN);
        return ele;
    }
}
