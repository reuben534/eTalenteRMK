import axios from 'axios';
import type { Portfolio, WithdrawalNotice } from './types';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
});

export async function getPortfolio(investorId: number) {
  const response = await api.get<Portfolio>(`/investors/${investorId}/portfolio`);
  return response.data;
}

export async function getWithdrawals(investorId: number) {
  const response = await api.get<WithdrawalNotice[]>('/withdrawals', { params: { investorId } });
  return response.data;
}

export async function createWithdrawal(investorId: number, productId: number, amount: number) {
  const response = await api.post<WithdrawalNotice>('/withdrawals', { investorId, productId, amount });
  return response.data;
}

export function statementUrl(investorId: number, from?: string, to?: string) {
  const baseUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
  const params = new URLSearchParams({ investorId: String(investorId) });
  if (from) params.set('from', from);
  if (to) params.set('to', to);
  return `${baseUrl}/withdrawals/statement.csv?${params.toString()}`;
}
