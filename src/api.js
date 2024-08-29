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

export function editName(name) {
  return api.post('/user/editName', { name });
}

export function editPassword(password) {
  return api.post('/user/editPassword', { password });
}

export function editEmail(email) {
  return api.post('/user/editEmail', { email });
}

export function editPhone(phone) {
  return api.post('/user/editPhone', { phone });
}