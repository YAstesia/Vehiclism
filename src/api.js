// src/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8081', //校园网10.208.112.75，oasis192.168.43.129
  headers: {
    'Content-Type': 'application/json',
  },
});

export const login = (key, pwd) => {
  return api.post('/user/login', {
    key,
    pwd,
  });
};

export const registerUser = async (userData) => {
  try {
    const response = await api.post('/user/register', userData);
    return response.data; // 假设后端返回 { success: true/false, msg: 'some message' }
  } catch (error) {
    console.error('Error during registration:', error);
    return { success: false, msg: '注册失败！请重试' };
  }
};

// api.js 中的函数实现示例
export function editEmail(id, email) {
  return api.post('/user/editEmail', { id, email });
}

export function editName(id, name) {
  return api.post('/user/editName', { id, name });
}

export function editPhone(id, phone) {
  return api.post('/user/editPhone', { id, phone });
}

export function getProducts(id) {
  return api.post('/user/likes', {id})
    .then(response => {
      if (response.data.success) {
        return response.data.data; // 返回产品数据
      } else {
        throw new Error(response.data.msg); // 抛出错误
      }
    });
}

export function getProductImage(id){
  return api.post('/cartirm/img', {id})
}

export const getCustomerData = async () => {
  try {
    const response = await api.get('/cartirm/tirms');
    if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.msg);
    }
  } catch (error) {
    console.error('Error fetching customer data:', error);
    return [];
  }
};

export const getSalesData = async () => {
  try {
    const response = await api.get('/search/AllSales');
    if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.msg);
    }
  } catch (error) {
    console.error('Error fetching customer data:', error);
    return [];
  }
};

export async function fetchSalesData() {
  try {
    const response = await api.get('/AllCarSale/Year');
    const data = response.data;

    if (data.success) {
      // 响应成功，处理数据
      return data.data.map(item => ({
        year: item.year,
        sales: item.totalSale
      }));
    } else {
      // 响应失败，抛出错误
      throw new Error(data.msg);
    }
  } catch (error) {
    console.error('Error fetching sales data:', error.message);
    return []; // 返回空数组以防止后续代码出错
  }
}

export async function fetchMonthData(year) {
  try {
    const response = await api.post('/AllCarSale/Month', {year});
    const data = response.data;

    if (data.success) {
      // 响应成功，处理数据
      return data.data.map(item => ({
        month: item.month,
        sales: item.totalSale
      }));
    } else {
      // 响应失败，抛出错误
      throw new Error(data.msg);
    }
  } catch (error) {
    console.error('Error fetching sales data:', error.message);
    return []; // 返回空数组以防止后续代码出错
  }
}

// 获取销量前10的省份数据
export function getTopProvinces() {
  return api.post('/CarRegionalSale/TopTenProvinces');
}

export function getProvinces() {
  return api.post('/CarRegionalSale/Provinces');
}
// 获取6个地区的销量占比数据
export function getRegionSales() {
  return api.post('/CarRegionalSale/Regions');
}
//获取销量前10的城市数据
export function getTopCities() {
  return api.post('/CarRegionalSale/TopTenCities');
}
// 获取省份城市销量数据
export function getProvinceCities(provinceName) {
  return api.post(`/CarRegionalSale/ProvinceCities`,{provinceName});
}
//获得全国品牌销量前10数据
export function getTopSeries() {
  return api.post('/CarRegionalSale/AllTopTenSeries');
}
// 获取省份品牌销量前10数据
export function getProvinceTopSeries(provinceName) {
  return api.post('/CarRegionalSale/ProvinceTopTenSeries',{provinceName});
}
// 获取城市品牌销量前10数据
export function getCityTopSeries(cityName) {
  return api.post('/CarRegionalSale/CityTopTenSeries',{cityName});
}
//通过省份获取城市
export function getCitiesFromProvince(provinceName){
  return api.post('/CarRegionalSale/getCitiesFromProvince',{provinceName});
}
export function getAllCities(){
  return api.post('/CarRegionalSale/getCities');
}

//搜索车型
export function SearchCarTirm(pageNum,pageSize,keyword){
  return api.post('/search/keyword',{pageNum,pageSize,keyword});
}

export function getBrandSaleMonth(year,month){
  return api.post('/getBrandSaleMonth',{year,month});
}
export function getBrandSaleYear(year){
  return api.post('/getBrandSaleYear',{year});
}
export function getSeriesSaleYear(year){
  return api.post('/carseries/getSeriesSaleYear',{year});
}
export function getSeriesSaleMonth(year,month){
  return api.post('/carseries/getSeriesSaleMonth',{year,month});
}

export function getSeriesPurpose(series){
  return api.post('/carseries/getSeriesPurpose',{series});
}

export function getCarSeries(series){
  return api.post('/carseries/getCarSeries',{series});
}

export function getCarSeriesImg(id){
  return api.post('/carseries/img',{id});
}
export function getCarEvl(id){
  return api.post('/carseries/evl',{id});
}
export function SearchCarSeriesSale(pageNum,pageSize,keyword){
  return api.post('/search/SaleKeyword',{pageNum,pageSize,keyword});
}
export function getCarSales(id,year){
  return api.post('/CarSales',{id,year});
}
export function getCarTirmBySeriesId(id){
  return api.post('/cartirm/getCarTirmBySeriesId',{id});
}

export function getCarTirm(tirm){
  return api.post('/cartirm/getCarTirm',{tirm});
}
export function getCarTirmImg(id){
  return api.post('/cartirm/img',{id});
}
export function getCarTirmConfig(id){
  return api.post('/cartirm/config',{id});
}
