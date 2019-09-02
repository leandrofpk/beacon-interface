package com.example.beacon.interfac.domain.service;

import com.example.beacon.interfac.domain.repository.ChainRepository;
import com.example.beacon.interfac.domain.chain.ChainValueObject;
import com.example.beacon.interfac.infra.ChainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveChainService {

    private final ChainRepository chainRepository;

    @Autowired
    public ActiveChainService(ChainRepository chainRepository) {
        this.chainRepository = chainRepository;
    }

    public ChainValueObject get(){
        ChainEntity entity = chainRepository.findTop1ByActive(true);
        return new ChainValueObject(entity.getVersion(), entity.getCipherSuite(), entity.getPeriod(), entity.getChainIndex());
    }

}
