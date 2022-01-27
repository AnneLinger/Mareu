package com.anne.linger.mareu.utils;

import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

/**
*Useful methods for chips
*/
public class ChipUtils {
    //Remove a chip from a view
    public void deleteAChipFromAView(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
    }

    //Remove a chip from a list
    public void deleteAChipFromAList(Chip chip, List<String> list) {
        String chipToRemove = chip.getText().toString();
        list.remove(chipToRemove);
    }

}
