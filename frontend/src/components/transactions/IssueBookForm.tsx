import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { Select } from '@/components/ui/select'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import { BookPlus } from 'lucide-react'
import type { Book, Member } from '@/types'

interface IssueBookFormProps {
  books: Book[];
  members: Member[];
  onSubmit: (bookId: number, memberId: number) => void;
  isLoading?: boolean;
}

export function IssueBookForm({ books, members, onSubmit, isLoading }: IssueBookFormProps) {
  const [bookId, setBookId] = useState<string>('')
  const [memberId, setMemberId] = useState<string>('')
  const [errors, setErrors] = useState<{ bookId?: string; memberId?: string }>({})

  const availableBooks = books.filter((b) => b.availableCopies > 0)
  const activeMembers = members.filter((m) => m.active)

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const newErrors: { bookId?: string; memberId?: string } = {}
    if (!bookId) newErrors.bookId = 'Please select a book'
    if (!memberId) newErrors.memberId = 'Please select a member'
    setErrors(newErrors)

    if (Object.keys(newErrors).length === 0) {
      onSubmit(Number(bookId), Number(memberId))
      setBookId('')
      setMemberId('')
    }
  }

  return (
    <Card className="max-w-lg">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <BookPlus className="h-5 w-5 text-teal-600" />
          Issue a Book
        </CardTitle>
        <CardDescription>
          Select a book and member to issue the book. Only available books and active members are shown.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="bookId">Book *</Label>
            <Select
              id="bookId"
              value={bookId}
              onChange={(e) => {
                setBookId(e.target.value)
                if (errors.bookId) setErrors((prev) => ({ ...prev, bookId: undefined }))
              }}
              className={errors.bookId ? 'border-red-500' : ''}
            >
              <option value="">Select a book...</option>
              {availableBooks.map((book) => (
                <option key={book.id} value={book.id}>
                  {book.title} by {book.author} ({book.availableCopies} available)
                </option>
              ))}
            </Select>
            {errors.bookId && <p className="text-xs text-red-500">{errors.bookId}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="memberId">Member *</Label>
            <Select
              id="memberId"
              value={memberId}
              onChange={(e) => {
                setMemberId(e.target.value)
                if (errors.memberId) setErrors((prev) => ({ ...prev, memberId: undefined }))
              }}
              className={errors.memberId ? 'border-red-500' : ''}
            >
              <option value="">Select a member...</option>
              {activeMembers.map((member) => (
                <option key={member.id} value={member.id}>
                  {member.name} ({member.memberType})
                </option>
              ))}
            </Select>
            {errors.memberId && <p className="text-xs text-red-500">{errors.memberId}</p>}
          </div>

          <Button type="submit" className="w-full" disabled={isLoading}>
            {isLoading ? 'Issuing...' : 'Issue Book'}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
