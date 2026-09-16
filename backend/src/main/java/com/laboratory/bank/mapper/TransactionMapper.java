package com.laboratory.bank.mapper;

import com.laboratory.bank.dto.TransactionDTO;
import com.laboratory.bank.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
 TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
 TransactionDTO toDTO(Transaction transaction);
}
