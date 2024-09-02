// src/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://192.168.43.129:8081', //或者ip
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