package com.ibay.tea.cms.service.system;

import com.ibay.tea.dao.system.SysMenuMapper;
import com.ibay.tea.entity.system.SysMenu;
import com.ibay.tea.entity.system.SysMenuRequest;
import com.ibay.tea.entity.system.SysMenuTree;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 系统菜单Service层

 */
@Service
@Slf4j
public class SysMenuService {

    /** 默认的分隔符 **/
    private static final String DEFAULT_SEPARATE = ",";
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private UserCacheService userCacheService;

    /**
     * 根据id获取菜单信息
     * @param menuId
     * @return
     */
    public SysMenu getSysMenuById(Integer menuId){
        if (null == menuId){
            return null;
        }
        return sysMenuMapper.getSysMenuById(menuId);
    }

    /**
     * 查询所有的菜单信息
     * @return
     */
    public List<SysMenu> getAllSysMenu(){
        return sysMenuMapper.getAllSysMenu();
    }

    /**
     * 保存菜单信息
     * @param sysMenu
     */
    public void saveSysMenu(SysMenuRequest sysMenu) {
        SysMenu menu = new SysMenu();
        SysMenu parentMenu = getSysMenuById(sysMenu.getParent().getId());
        if (null != parentMenu){
            String parentIds = String.format("%s%s%s",parentMenu.getParentIds(), parentMenu.getId(), DEFAULT_SEPARATE);
            BeanUtils.copyProperties(sysMenu, menu);
            menu.setParentIds(parentIds);
            menu.setParentId(sysMenu.getParent().getId());
            menu.setCreateBy(String.valueOf(userCacheService.getSysUser().getId()));
            menu.setUpdateBy(String.valueOf(userCacheService.getSysUser().getId()));
            menu.setCreateDate(new Date());
            menu.setUpdateDate(new Date());
        }
        sysMenuMapper.save(menu);
    }

    /**
     * 更新菜单信息
     * @param sysMenu
     */
    public void updateSysMenu(SysMenuRequest sysMenu) {
        SysMenu menu = new SysMenu();
        SysMenu parentMenu = getSysMenuById(sysMenu.getParent().getId());
        if (null != parentMenu){
            String parentIds = String.format("%s%s%s",parentMenu.getParentIds(), parentMenu.getId(), DEFAULT_SEPARATE);
            BeanUtils.copyProperties(sysMenu, menu);
            menu.setUpdateBy(String.valueOf(userCacheService.getSysUser().getId()));
            menu.setUpdateDate(new Date());
            menu.setParentIds(parentIds);
        }
        Integer menuId = sysMenu.getId();
        String oldParentIds = sysMenu.getParentIds();
        List<SysMenu> sysMenuList = sysMenuMapper.getSysMenuByParentIds(String.valueOf(menuId));
        if (!CollectionUtils.isEmpty(sysMenuList)){
            sysMenuList.forEach(subMenu -> {
                String subMenuParentIds = subMenu.getParentIds();
                subMenu.setParentIds(subMenuParentIds.replace(oldParentIds, menu.getParentIds()));
                sysMenuMapper.update(sysMenu);
            });
        }
        sysMenuMapper.update(menu);
    }

    /**
     * 删除菜单信息
     * @param id
     */
    public void deleteSysMenu(Integer id) {
        sysMenuMapper.deleteSysMenuById(id, null, new Date());
    }

    /**
     * 构造SysMenuRequest对象
     * @param sysMenuList
     * @return
     */
    public List<SysMenuRequest> buildSysMenuRequest(List<SysMenu> sysMenuList){
        List<SysMenuRequest> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(sysMenuList)){
            return result;
        }
        sysMenuList.forEach(sysMenu -> {
            Integer parentId = sysMenu.getParentId();
            SysMenuRequest sysMenuRequest = new SysMenuRequest();
            BeanUtils.copyProperties(sysMenu, sysMenuRequest);
            sysMenuRequest.setParent(getSysMenuById(parentId));

            result.add(sysMenuRequest);
        });

        return result;
    }

    /**
     * 获取用户权限
     * @param menuId
     * @return
     */
    public List<Map<String, String>> getSysMenuAuthor(String menuId){
        List<Map<String, String>> resultList = new ArrayList<>();
        if (StringUtils.isBlank(menuId)) {
            return resultList;
        }
        List<SysMenu> sysMenuList = sysMenuMapper.getSysMenuByParentIds(menuId);
        if (CollectionUtils.isEmpty(sysMenuList)){
            return resultList;
        }

        sysMenuList.forEach(menu -> {
            String parentIds = menu.getParentIds();
            int length = parentIds.split(DEFAULT_SEPARATE).length;
            if (length == 5){
                Map<String, String> objMap = new HashMap<>(2);
                objMap.put("name", menu.getName());
                objMap.put("buttonAuthor", menu.getHref());
                resultList.add(objMap);
            }

        });
        return resultList;
    }


    /**
     * 构造map数据
     * @param sysMenuList
     * @return
     */
    private Map<Integer,List<SysMenuRequest>> buildMap(List<SysMenuRequest> sysMenuList){
        Map<Integer,List<SysMenuRequest>> map = new HashMap<>(5);

        sysMenuList.forEach(menu -> {
            String parentIds = menu.getParentIds();
            int length = parentIds.split(DEFAULT_SEPARATE).length;
            List<SysMenuRequest> requestList = null;
            if (!map.containsKey(length)){
                requestList = new ArrayList<>();
            } else {
                requestList = map.get(length);
            }
            requestList.add(menu);
            map.put(length, requestList);
        });
        return map;

    }

    /**
     * 获取菜单树
     * @param menuId
     * @return
     */
    public SysMenuTree querySysMenuTree(String menuId, boolean showFiveMenu){
        SysMenuTree sysMenuTree = null;
        List<SysMenuRequest> menuList = userCacheService.getSysMenuList();
        if (CollectionUtils.isEmpty(menuList)){
            return null;
        }
        Map<Integer,List<SysMenuRequest>> map = buildMap(menuList);

        SysMenuRequest topMenu = map.get(1).get(0);
        sysMenuTree = new SysMenuTree(topMenu.getId(), topMenu.getName(), topMenu.getParentId(), topMenu.getHref(),
                topMenu.getIcon(), topMenu.getIsShow(), topMenu.getPermission(), topMenu.getParentIds(), topMenu.getSort());
        List<SysMenuTree> result = new ArrayList<>();
        map.get(2).forEach(secondMenu -> {
            SysMenuTree secondMenuTree = new SysMenuTree(secondMenu.getId(), secondMenu.getName(),
                    secondMenu.getParentId(), secondMenu.getHref(), secondMenu.getIcon(), secondMenu.getIsShow(),
                    secondMenu.getPermission(), secondMenu.getParentIds(), secondMenu.getSort());
            List<SysMenuTree> secondSubMenuList = new ArrayList<>();
            map.get(3).forEach(thirdMenu -> {
                if (thirdMenu.getParentId() == secondMenu.getId()) {
                    SysMenuTree thirdMenuTree = new SysMenuTree(thirdMenu.getId(), thirdMenu.getName(),
                            thirdMenu.getParentId(), thirdMenu.getHref(), thirdMenu.getIcon(), thirdMenu.getIsShow(),
                            thirdMenu.getPermission(), thirdMenu.getParentIds(), thirdMenu.getSort());
                    List<SysMenuTree> thirdSubMenuList = new ArrayList<>();
                    map.get(4).forEach(fourthMenu -> {
                        if (fourthMenu.getParentId() == thirdMenu.getId()){
                            SysMenuTree fourthMenuTree = new SysMenuTree(fourthMenu.getId(), fourthMenu.getName(),
                                    fourthMenu.getParentId(), fourthMenu.getHref(), fourthMenu.getIcon(),
                                    fourthMenu.getIsShow(), fourthMenu.getPermission(), fourthMenu.getParentIds(), fourthMenu.getSort());
                            thirdSubMenuList.add(fourthMenuTree);
                            if (showFiveMenu) {
                                List<SysMenuTree> fourthSubMenuList = new ArrayList<>();
                                map.get(5).forEach(fiveMenu -> {
                                    if (fiveMenu.getParentId() == fourthMenu.getId()) {
                                        SysMenuTree fiveMenuTree = new SysMenuTree(fiveMenu.getId(), fiveMenu.getName(),
                                                fiveMenu.getParentId(), fiveMenu.getHref(), fiveMenu.getIcon(),
                                                fiveMenu.getIsShow(), fiveMenu.getPermission(), fiveMenu.getParentIds(), fiveMenu.getSort());
                                        fiveMenuTree.setSubMenuList(new ArrayList<>());
                                        fourthSubMenuList.add(fiveMenuTree);
                                    }
                                });
                                Collections.sort(fourthSubMenuList);
                                fourthMenuTree.setSubMenuList(fourthSubMenuList);
                            } else {
                                fourthMenuTree.setSubMenuList(new ArrayList<>());
                            }
                        }
                    });
                    Collections.sort(thirdSubMenuList);
                    thirdMenuTree.setSubMenuList(thirdSubMenuList);
                    secondSubMenuList.add(thirdMenuTree);
                }
            });
            Collections.sort(secondSubMenuList);
            secondMenuTree.setSubMenuList(secondSubMenuList);
            result.add(secondMenuTree);
        });
        Collections.sort(result);
        sysMenuTree.setSubMenuList(result);

        return sysMenuTree;
    }
    /**
     * 获取菜单树形结构
     * @param menuId
     */
    public List<Map<String, Object>> getSysMenuTree(String menuId) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<SysMenuRequest> menuList = userCacheService.getSysMenuList();
        if (CollectionUtils.isEmpty(menuList)){
            return mapList;
        }
        menuList.forEach(menu -> {
            if (menuId == null
                    || (menuId != null && !menuId.equals(menu.getId())
                    && menu.getParentIds().indexOf("," + menuId + ",") == -1)){
                Map<String, Object> map = new HashMap<>(16);
                map.put("id", menu.getId());
                map.put("parentId", menu.getParent() != null ? menu.getParent().getId() : 0);
                map.put("name", menu.getName());
                mapList.add(map);
            }
        });
        return mapList;
    }
}
