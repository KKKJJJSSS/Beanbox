package Beanbox.Beanbox.service;

import Beanbox.Beanbox.dto.BeanDto;
import Beanbox.Beanbox.dto.CartDto;
import Beanbox.Beanbox.model.BeanMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeanService {
    private final BeanMapper beanMapper;

    public BeanService(BeanMapper beanMapper) {
        this.beanMapper = beanMapper;
    }

    public List<BeanDto> getBeanList() {
        return beanMapper.getBeanList();
    }

    public int findByLastBean(String beanName) {
        return beanMapper.findByLastBean(beanName);
    };

    public void deleteLastBean(String beanName) {
        beanMapper.deleteLastBean(beanName);
    };
}
