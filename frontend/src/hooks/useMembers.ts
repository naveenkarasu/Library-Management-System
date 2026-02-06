import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getMembers, getMember, createMember, updateMember, deleteMember } from '@/api/members'
import type { MemberFormData } from '@/types'

export function useMembers() {
  return useQuery({
    queryKey: ['members'],
    queryFn: getMembers,
  })
}

export function useMember(id: number) {
  return useQuery({
    queryKey: ['members', id],
    queryFn: () => getMember(id),
    enabled: id > 0,
  })
}

export function useCreateMember() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data: MemberFormData) => createMember(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['members'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
  })
}

export function useUpdateMember() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: MemberFormData }) => updateMember(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['members'] })
    },
  })
}

export function useDeleteMember() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => deleteMember(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['members'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
  })
}
