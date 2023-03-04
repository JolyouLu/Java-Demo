package one.model;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 17:01
 * @Description
 */
public class NumberComboBoxListModel extends AbstractListModel<BigDecimal> implements ComboBoxModel<BigDecimal> {

    private int selectedId;

    BigDecimal start;
    BigDecimal end;
    BigDecimal step;

    public NumberComboBoxListModel(BigDecimal start, BigDecimal end, BigDecimal step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof BigDecimal){
            BigDecimal curr = (BigDecimal) anItem;
            selectedId = curr.subtract(start).divide(step,2,RoundingMode.HALF_DOWN).intValue();
        }
    }

    @Override
    public Object getSelectedItem() {
        BigDecimal item = new BigDecimal(selectedId).multiply(step).add(start).setScale(1, RoundingMode.HALF_DOWN);
        return item;
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
