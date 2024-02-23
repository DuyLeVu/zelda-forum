package com.zelda.service.Impl;

import com.zelda.model.entity.VerificationToken;
import com.zelda.repository.VerificationTokenRepository;
import com.zelda.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void save(VerificationToken token) {
        verificationTokenRepository.save(token);
    }
}
