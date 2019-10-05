package com.ibay.tea.api.service.calculate;

import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.responseVo.CalculateReturnVo;

public interface CalculateService {

    CalculateReturnVo calculateCartOrderPrice(CartOrderParamVo cartOrderParamVo, boolean isCreateOrder);
}
