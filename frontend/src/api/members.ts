import apiClient from './client'
import type { Member, MemberFormData } from '@/types'

export async function getMembers(): Promise<Member[]> {
  const response = await apiClient.get<Member[]>('/api/v1/members')
  return response.data
}

export async function getMember(id: number): Promise<Member> {
  const response = await apiClient.get<Member>(`/api/v1/members/${id}`)
  return response.data
}

export async function createMember(data: MemberFormData): Promise<Member> {
  const response = await apiClient.post<Member>('/api/v1/members', data)
  return response.data
}

export async function updateMember(id: number, data: MemberFormData): Promise<Member> {
  const response = await apiClient.put<Member>(`/api/v1/members/${id}`, data)
  return response.data
}

export async function deleteMember(id: number): Promise<void> {
  await apiClient.delete(`/api/v1/members/${id}`)
}
