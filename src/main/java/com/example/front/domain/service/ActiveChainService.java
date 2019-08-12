package com.example.front.domain.service;

import com.example.front.domain.chain.ChainValueObject;
import com.example.front.domain.repository.ChainRepository;
import com.example.front.infra.ChainEntity;
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
