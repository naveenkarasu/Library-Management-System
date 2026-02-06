import { useBooks } from '@/hooks/useBooks'
import { useMembers } from '@/hooks/useMembers'
import { useIssueBook } from '@/hooks/useTransactions'
import { IssueBookForm } from '@/components/transactions/IssueBookForm'
import { Spinner } from '@/components/ui/spinner'
import { useToast } from '@/components/ui/toast'

export function IssueBookPage() {
  const { data: books, isLoading: booksLoading } = useBooks()
  const { data: members, isLoading: membersLoading } = useMembers()
  const issueBook = useIssueBook()
  const { addToast } = useToast()

  const handleSubmit = (bookId: number, memberId: number) => {
    issueBook.mutate(
      { bookId, memberId },
      {
        onSuccess: () => {
          addToast('Book issued successfully', 'success')
        },
        onError: () => {
          addToast('Failed to issue book', 'error')
        },
      }
    )
  }

  if (booksLoading || membersLoading) {
    return <Spinner className="py-20" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-stone-900">Issue Book</h1>
        <p className="text-sm text-muted-foreground mt-1">
          Issue a book to a library member
        </p>
      </div>

      <IssueBookForm
        books={books ?? []}
        members={members ?? []}
        onSubmit={handleSubmit}
        isLoading={issueBook.isPending}
      />
    </div>
  )
}
