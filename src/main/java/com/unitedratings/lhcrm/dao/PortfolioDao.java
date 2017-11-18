package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wangyongxin
 */
public interface PortfolioDao extends JpaRepository<Portfolio,Long>{

    /**
     * 修改资产池关联的理想违约率版本
     * @param id
     * @param idealDefaultId
     * @return
     */
    @Modifying
    @Query("update Portfolio p set p.idealDefaultId = ?2 where p.id = ?1")
    int updatePortfolioById(Long id, Integer idealDefaultId);

    /**
     * 更新资产池已模拟次数
     * @param id
     * @param simulationNum
     */
    @Modifying
    @Query("update Portfolio p set p.simulationNum = ?2 where p.id = ?1")
    void updateSimulationNum(Long id, int simulationNum);

    /**
     * 根据资产池名称模糊查询并分页
     * @param portfolioName
     * @param num
     * @param pageable
     * @return
     */
    Page<Portfolio> findByPortfolioNameContainsAndSimulationNumGreaterThanOrderByCreateTimeDesc(String portfolioName, int num, Pageable pageable);
    /**
     * 根据资产池名称模糊、用户id查询并分页
     * @param userId
     * @param portfolioName
     * @param num
     * @param pageable
     * @return
     */
    Page<Portfolio> findByUserIdAndPortfolioNameContainsAndSimulationNumGreaterThanOrderByCreateTimeDesc(Integer userId , String portfolioName, int num, Pageable pageable);
    /**
     * 根据用户id查询并分页
     * @param userId
     * @param num
     * @param pageable
     * @return
     */
    Page<Portfolio> findByUserIdAndSimulationNumGreaterThanOrderByCreateTimeDesc(Integer userId,int num,Pageable pageable);
    /**
     * 分页查询有模拟记录的资产池列表
     * @param num
     * @param pageable
     * @return
     */
    Page<Portfolio> findBySimulationNumGreaterThanOrderByCreateTimeDesc(int num,Pageable pageable);
    /**
     * 根据条件统计记录数
     * @param simulationNum
     * @param userId
     * @param portfolioName
     * @return
     */
    int countBySimulationNumGreaterThanAndUserIdAndPortfolioNameLike(int simulationNum,Integer userId,String portfolioName);

    /**
     * 根据条件统计记录数
     * @param simulationNum
     * @return
     */
    int countBySimulationNumGreaterThan(int simulationNum);

    /**
     * 根据条件统计记录数
     * @param simulationNum
     * @param portfolioName
     * @return
     */
    int countBySimulationNumGreaterThanAndPortfolioNameLike(int simulationNum, String portfolioName);

    /**
     * 根据条件统计记录数
     * @param simulationNum
     * @param userId
     * @return
     */
    int countBySimulationNumGreaterThanAndUserId(int simulationNum, Integer userId);
}
