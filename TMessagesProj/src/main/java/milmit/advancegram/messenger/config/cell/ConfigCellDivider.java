package milmit.advancegram.messenger.config.cell;

import androidx.recyclerview.widget.RecyclerView;

import milmit.advancegram.messenger.config.CellGroup;

public class ConfigCellDivider extends AbstractConfigCell {

    public int getType() {
        return CellGroup.ITEM_TYPE_DIVIDER;
    }

    public boolean isEnabled() {
        return false;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder) {
    }
}
