package com.smlyk.eshopinventory.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author always
 * @since 2021-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProductInventory extends Model {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;

    private Integer inventoryCnt;


}
