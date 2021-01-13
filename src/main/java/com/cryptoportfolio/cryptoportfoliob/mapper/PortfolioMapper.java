package com.cryptoportfolio.cryptoportfoliob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cryptoportfolio.cryptoportfoliob.dto.PortfolioDTO;
import com.cryptoportfolio.cryptoportfoliob.dto.UserRegistrationDTO;
import com.cryptoportfolio.cryptoportfoliob.entity.Portfolio;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {

	@Mapping(target = "coins", ignore = true)
	Portfolio toEntity(UserRegistrationDTO registrationDTO);

	PortfolioDTO toDTO(Portfolio portfolio);
}