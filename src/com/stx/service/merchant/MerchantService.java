package com.stx.service.merchant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stx.dao.MerchantDao;
import com.stx.entity.Constants;
import com.stx.entity.merchant.Merchant;
import com.stx.entity.user.Collection;
import com.stx.service.admin.PunishService;
import com.stx.service.user.CollectionService;
import com.stx.util.CollectionUtils;
import com.stx.util.StringUtils;

/**
 * 商户信息管理
 * 
 * @author gzzdsg
 * 2016年3月10日
 */
@Service
public class MerchantService {

	@Autowired
	private MerchantDao merchantDao;
	@Autowired
	private CollectionService collectionService;
	@Autowired
	private PunishService punishService;
	
	/**
	 * 查询用户收藏的商家
	 * @param userId
	 * @return
	 */
	public List<Merchant> getCollectionMerchant(Long userId){
		List<Collection> collectionList = collectionService.findCollectionByUserId(userId);
		if(CollectionUtils.isEmpty(collectionList)){
			return null;
		}
		List<Merchant> reList = new ArrayList<Merchant>();
		for(Collection collection : collectionList){
			Merchant merchant = merchantDao.findMerchantByMerchantId(collection.getMerchantId());
			if(merchant.getOpenFlag() == Constants.FLAG_OPEN){
				reList.add(merchant);
			}
		}
		return reList;
	}
	
	/**
	 * 完成订单是增加月销量
	 * @param merchantId
	 */
	public void addMonthCount(Long merchantId){
		Merchant merchant = merchantDao.findMerchantByMerchantId(merchantId);
		merchant.setMonthCount(merchant.getMonthCount() + 1);
		merchantDao.updateMerchantInfo(merchant);
	}
	
	/**
	 * 查找热门商户
	 * @return
	 */
	public List<Merchant> queryHotMerchant(){
		return merchantDao.findHotMerchants();
	}
	
	/**
	 * 根据商户id查找对应的商铺
	 * 
	 * @param userId
	 * @return
	 */
	public Merchant getMerchantByUserId(Long userId){
		Merchant merchant = merchantDao.findMerchant(userId);
		if(merchant==null){
			merchant = new Merchant();
		}
		return merchant;
	}
	
	
	/**
	 * 根据商户类型查找
	 * @param type
	 * @return
	 */
	public List<Merchant> getMerchantByType(String type){
		return merchantDao.findMerchantByType(type);
	}
	
	/**
	 * 根据商户类型查找3个商铺
	 * @param type
	 * @return
	 */
	public List<Merchant> get3MerchantByType(String type){
		return merchantDao.findMerchantByType(type);
	}
	
	/**
	 * 根据商户id查询商户信息
	 * @param merchantId
	 * @return
	 */
	public Merchant getMerchantByMerchantId(Long merchantId){
		return merchantDao.findMerchantByMerchantId(merchantId);
	}
	
	/**
	 * 根据ids查询商户们
	 * @param ids
	 * @return
	 */
	public List<Merchant> queryMerchantList(List<Long> ids){
		return merchantDao.findMerchantsByIds(ids);
	}
	
	public String updateLogo(File logo,String logoPath,String logoName){
		
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(logo));
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					new File(logoPath, logoName)));
			OutputStream os2 = new BufferedOutputStream(new FileOutputStream(new File("D:"+File.separator+"stwj"+File.separator+"bysj"+File.separator+"WebContent"+File.separator+"image"+File.separator+"logo"+ File.separator +logoName)));
			byte[] buffer = new byte[500];
			while (-1 != (is.read(buffer, 0, buffer.length))) {
				os.write(buffer);
				os2.write(buffer);
			}
			os.close();
			os2.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "logo保存失败。";
		}
		return null;
	}
	
	/**
	 * 修改商户信息
	 * @param newMerchant
	 * @param oldMerchant
	 * @return
	 */
	public String updateMerchantInfo(Merchant newMerchant,Merchant oldMerchant){
		newMerchant.setId(oldMerchant.getId());
		newMerchant.setUserId(oldMerchant.getUserId());
		newMerchant.setMonthCount(oldMerchant.getMonthCount());
		Double hour = punishService.validatePunish(oldMerchant.getId());
		if(hour  != 0d){
			return "您的商户已被禁闭，无法修改信息，"+ hour +"小时后才可以修改信息";
		}
		if(newMerchant.getName()==null||newMerchant.getName().trim().length()<1){
			return "商户名称不能为空";
		}
		if(newMerchant.getMobile()==null||newMerchant.getMobile().trim().length()<1){
			return "商户联系方式不能为空";
		}
		if(newMerchant.getMerchantAddr()==null||newMerchant.getMerchantAddr().trim().length()<1){
			return "商户地址不能为空";
		}
		if(newMerchant.getSlogen()==null||newMerchant.getSlogen().trim().length()<1){
			return "商户广告语不能为空";
		}
		if(newMerchant.getDeliveryStart()==null){
			return "商户起送价不能为空";
		}
		if(newMerchant.getDeliveryPrice()==null){
			return "商户配送费不能为空";
		}
		if(newMerchant.getBoxPrice()==null){
			return "餐盒费不能为空";
		}
		if(newMerchant.getDeliveryTime()==null){
			return "商户配送时间不能为空";
		}
		if(newMerchant.getWorkTime()==null||newMerchant.getWorkTime().trim().length()<1){
			return "工作时间不能为空";
		}
		merchantDao.updateMerchantInfo(newMerchant);
		return null;
	}
	
	/**
	 * 生成一个默认的商户信息
	 * @return
	 */
	public Long generateMerchant(Long userId){
		Merchant merchant = new Merchant();
		merchant.setBoxPrice(1f);
		merchant.setCodFlag(2);
		merchant.setDeliveryPrice(5f);
		merchant.setDeliveryStart(10f);
		merchant.setDeliveryTime(30);
		merchant.setLogoAddr("logo.jpg");
		merchant.setMerchantAddr("请输入商户地址");
		merchant.setMerchantType("");
		merchant.setMobile("请输入联系手机");
		merchant.setMonthCount(0);
		merchant.setName("请输入商户名称");
		merchant.setOpenFlag(2);
		merchant.setSlogen("请输入广告语");
		merchant.setUserId(userId);
		merchant.setWorkTime("请输入营业时间");
		return merchantDao.saveMerchant(merchant);
	}
	
	/**
	 * 查找禁闭商户展示的数据
	 * @param merchantCategory
	 * @param merchantName
	 * @return
	 */
	public List<Merchant> pageAllMerchant(String merchantType ,String merchantName){
		if(StringUtils.isNotEmpty(merchantName)){
			return merchantDao.findMerchantByName(merchantName);
		}
		if(StringUtils.isNotEmpty(merchantType)&&!"0".equals(merchantType)){
			return merchantDao.adminFindMerchantByType(merchantType);
		}
		return merchantDao.findAllMerchant();
	}
	
	/**
	 * 改变商户上线状态
	 * @param merchantId
	 * @param flag
	 */
	public void changeMerchantOpen (Long merchantId, Integer flag){
		Merchant merchant = merchantDao.findMerchantByMerchantId(merchantId);
		merchant.setOpenFlag(flag);
		merchantDao.updateMerchantInfo(merchant);
	}
	
}
