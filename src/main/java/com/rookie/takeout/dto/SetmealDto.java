package com.rookie.takeout.dto;

import com.rookie.takeout.entity.Setmeal;
import com.rookie.takeout.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
