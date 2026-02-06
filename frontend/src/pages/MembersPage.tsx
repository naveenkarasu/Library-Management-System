import { useState, useMemo } from 'react'
import { useMembers, useCreateMember, useUpdateMember, useDeleteMember } from '@/hooks/useMembers'
import { MemberTable } from '@/components/members/MemberTable'
import { MemberForm } from '@/components/members/MemberForm'
import { BookSearch } from '@/components/books/BookSearch'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Spinner } from '@/components/ui/spinner'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog'
import { useToast } from '@/components/ui/toast'
import { Plus } from 'lucide-react'
import type { Member, MemberFormData } from '@/types'

export function MembersPage() {
  const { data: members, isLoading } = useMembers()
  const createMember = useCreateMember()
  const updateMember = useUpdateMember()
  const deleteMember = useDeleteMember()
  const { addToast } = useToast()

  const [search, setSearch] = useState('')
  const [formOpen, setFormOpen] = useState(false)
  const [editingMember, setEditingMember] = useState<Member | null>(null)
  const [deleteConfirm, setDeleteConfirm] = useState<Member | null>(null)

  const filteredMembers = useMemo(() => {
    if (!members) return []
    if (!search.trim()) return members
    const query = search.toLowerCase()
    return members.filter(
      (member) =>
        member.name.toLowerCase().includes(query) ||
        member.email.toLowerCase().includes(query) ||
        member.phone.includes(query)
    )
  }, [members, search])

  const handleAdd = () => {
    setEditingMember(null)
    setFormOpen(true)
  }

  const handleEdit = (member: Member) => {
    setEditingMember(member)
    setFormOpen(true)
  }

  const handleFormSubmit = (data: MemberFormData) => {
    if (editingMember) {
      updateMember.mutate(
        { id: editingMember.id, data },
        {
          onSuccess: () => {
            setFormOpen(false)
            setEditingMember(null)
            addToast('Member updated successfully', 'success')
          },
          onError: () => {
            addToast('Failed to update member', 'error')
          },
        }
      )
    } else {
      createMember.mutate(data, {
        onSuccess: () => {
          setFormOpen(false)
          addToast('Member added successfully', 'success')
        },
        onError: () => {
          addToast('Failed to add member', 'error')
        },
      })
    }
  }

  const handleDelete = (member: Member) => {
    setDeleteConfirm(member)
  }

  const confirmDelete = () => {
    if (!deleteConfirm) return
    deleteMember.mutate(deleteConfirm.id, {
      onSuccess: () => {
        setDeleteConfirm(null)
        addToast('Member deleted successfully', 'success')
      },
      onError: () => {
        addToast('Failed to delete member', 'error')
      },
    })
  }

  if (isLoading) {
    return <Spinner className="py-20" />
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-stone-900">Members</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Manage library members
          </p>
        </div>
        <Button onClick={handleAdd}>
          <Plus className="h-4 w-4 mr-2" />
          Add Member
        </Button>
      </div>

      <Card>
        <CardHeader>
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <CardTitle>Member List</CardTitle>
            <div className="w-full sm:w-72">
              <BookSearch
                value={search}
                onChange={setSearch}
                placeholder="Search members..."
              />
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <MemberTable
            members={filteredMembers}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        </CardContent>
      </Card>

      <MemberForm
        open={formOpen}
        onClose={() => {
          setFormOpen(false)
          setEditingMember(null)
        }}
        onSubmit={handleFormSubmit}
        member={editingMember}
        isLoading={createMember.isPending || updateMember.isPending}
      />

      <Dialog open={!!deleteConfirm} onOpenChange={() => setDeleteConfirm(null)}>
        <DialogContent onClose={() => setDeleteConfirm(null)} className="max-w-sm">
          <DialogHeader>
            <DialogTitle>Delete Member</DialogTitle>
          </DialogHeader>
          <p className="text-sm text-muted-foreground">
            Are you sure you want to delete &quot;{deleteConfirm?.name}&quot;? This action cannot be undone.
          </p>
          <DialogFooter>
            <Button variant="outline" onClick={() => setDeleteConfirm(null)}>
              Cancel
            </Button>
            <Button
              variant="destructive"
              onClick={confirmDelete}
              disabled={deleteMember.isPending}
            >
              {deleteMember.isPending ? 'Deleting...' : 'Delete'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}
