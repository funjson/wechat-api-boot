package cn.funjson.wechatapiboot.api.dto.button;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ButtonsDto extends MenuDto{

    private static final int MAX_SIZE=3;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<MenuDto> button=new ArrayList<>();

    @Override
    public void add(MenuDto menuDto) {

        if (button.size()>3){
            throw new IllegalArgumentException("一级菜单数量不能超过"+MAX_SIZE);
        }
        button.add(menuDto);
    }
}
