package cn.funjson.wechatapiboot.api.dto.button;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ButtonDto extends MenuDto{

    private static final int MAX_SIZE = 5;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<MenuDto> sub_button=new ArrayList<>();

    @Override
    public void add(MenuDto menuDto) {

        if (sub_button.size()>MAX_SIZE){
            throw new IllegalArgumentException("二级菜单数量不能超过"+MAX_SIZE);
        }
        sub_button.add(menuDto);
    }
}
