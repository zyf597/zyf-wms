package com.zyf.web.controller.common;

import com.zyf.common.core.controller.BaseController;
import com.zyf.common.core.domain.AjaxResult;
import com.zyf.system.service.PhoneCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/code")
public class CodeController extends BaseController {
    @Value("${zyf.traceIdName}")
    private String traceIdName;
    @Autowired
    private PhoneCodeService phoneCodeService;

    @GetMapping("/get")
    public AjaxResult getCode(HttpServletRequest request, @RequestParam String phone) {
        String uuid = request.getHeader(traceIdName);
        boolean res  = phoneCodeService.obtainCode(uuid, phone);
        return AjaxResult.success(res);
    }
}
