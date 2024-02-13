package Beanbox.Beanbox.model;

import Beanbox.Beanbox.dto.BeanDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BeanMapper {
    @Select("SELECT * FROM coffeebean")
    List<BeanDto> getBeanList();

    @Select("SELECT count(*) FROM recipe WHERE bean_name = #{bean_name}")
    int findByLastBean(@Param("bean_name") String beanName);

    @Delete("DELETE FROM coffeebean WHERE bean_name = #{bean_name}")
    void deleteLastBean(String beanName);
}


