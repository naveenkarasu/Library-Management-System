import apiClient from './client'
import type { LoginRequest, LoginResponse } from '@/types'

export async function login(data: LoginRequest): Promise<LoginResponse> {
  const response = await apiClient.post<LoginResponse>('/api/v1/auth/login', data)
  return response.data
}

export async function register(data: { username: string; password: string; role: string }): Promise<void> {
  await apiClient.post('/api/v1/auth/register', data)
}
