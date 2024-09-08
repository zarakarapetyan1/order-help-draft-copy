package com.platform.controller;


import com.platform.RequiredImporterType;
import com.platform.constants.RoutConstants;
import com.platform.model.Product;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.VERSION + RoutConstants.PRODUCTS)
public class ProductController {

    @PostMapping
    @RequiredImporterType
    public @ResponseBody String create(@RequestBody Product product, Principal request) {
        String header = request.getName();
        return header;
    }
}
