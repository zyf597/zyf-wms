package com.zyf.convert;

import org.mapstruct.Mapper;
import com.zyf.domain.entity.Wave;
import com.zyf.domain.vo.WaveVO;
import java.util.List;
/**
 * 波次  DO <=> DTO <=> VO / BO / Query
 *
 * @author zyf
 */
@Mapper(componentModel = "spring")
public interface WaveConvert  {

    List<WaveVO> dos2vos(List<Wave> list);
}
