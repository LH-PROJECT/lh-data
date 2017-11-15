package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
     * @param begin
     * @param size
     * @return
     */
    @Query(value = "SELECT p.* FROM portfolio p WHERE p.simulation_num > 0 AND (p.portfolio_name LIKE concat(?1,'%') OR p.portfolio_name LIKE '%'+?1) ORDER BY p.create_time LIMIT ?2,?3 ", nativeQuery = true)
    List<Portfolio> findPageByPortfolioName(String portfolioName,int begin,int size);

    /**
     * 根据资产池名称模糊、用户id查询并分页
     * @param userId
     * @param portfolioName
     * @param begin
     * @param size
     * @return
     */
    @Query(value = "SELECT p.* FROM portfolio p WHERE p.simulation_num > 0 AND p.user_id = ?1 AND (p.portfolio_name LIKE concat(?2,'%') OR p.portfolio_name LIKE '%'+?2) ORDER BY p.create_time LIMIT ?3,?4 ", nativeQuery = true)
    List<Portfolio> findPageByUserIdAndPortfolioName(Integer userId ,String portfolioName,int begin,int size);

    /**
     * 根据用户id查询并分页
     * @param userId
     * @param begin
     * @param pageSize
     * @return
     */
    @Query(value = "SELECT p.* FROM portfolio p WHERE p.simulation_num > 0 AND p.user_id = ?1 ORDER BY p.create_time LIMIT ?2,?3 ", nativeQuery = true)
    List<Portfolio> findPageByUserId(Integer userId, int begin, Integer pageSize);

    /**
     * 分页查询有模拟记录的资产池列表
     * @param begin
     * @param pageSize
     * @return
     */
    @Query(value = "SELECT p.* FROM portfolio p WHERE p.simulation_num > 0 ORDER BY p.create_time LIMIT ?1,?2 ", nativeQuery = true)
    List<Portfolio> findPageOrderByCreateTime(int begin, Integer pageSize);

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
