package com.tuling.security.controller;

import com.tuling.security.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security/encryption")
public class EncryptionController {

    @Autowired
    private EncryptionService encryptionService;

    @PostMapping("/publicKey")
    public String getPublicKey(@RequestParam("sessionId") String sessionId) {
        return encryptionService.generateAndStoreKeyPair(sessionId);
    }
}