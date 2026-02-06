import { useState } from 'react'
import { useActiveTransactions, useReturnBook } from '@/hooks/useTransactions'
import { ReturnBookForm } from '@/components/transactions/ReturnBookForm'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import { Spinner } from '@/components/ui/spinner'
import { useToast } from '@/components/ui/toast'
import { Undo2 } from 'lucide-react'
import { BookReturnAnim3D } from '@/components/three/BookReturnAnim3D'

export function ReturnBookPage() {
  const { data: transactions, isLoading } = useActiveTransactions()
  const returnBook = useReturnBook()
  const { addToast } = useToast()
  const [returningId, setReturningId] = useState<number | null>(null)

  const handleReturn = (transactionId: number) => {
    setReturningId(transactionId)
    returnBook.mutate(transactionId, {
      onSuccess: () => {
        addToast('Book returned successfully', 'success')
        setReturningId(null)
      },
      onError: () => {
        addToast('Failed to return book', 'error')
        setReturningId(null)
      },
    })
  }

  return (
    <div className="space-y-6">
      <BookReturnAnim3D />

      <div>
        <h1 className="text-2xl font-bold text-teal-950">Return Book</h1>
        <p className="text-sm text-muted-foreground mt-1">
          Process book returns from library members
        </p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Undo2 className="h-5 w-5 text-teal-600" />
            Active Transactions
          </CardTitle>
          <CardDescription>
            Select a transaction to process the book return
          </CardDescription>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <Spinner className="py-12" />
          ) : (
            <ReturnBookForm
              transactions={transactions ?? []}
              onReturn={handleReturn}
              isLoading={returnBook.isPending}
              returningId={returningId}
            />
          )}
        </CardContent>
      </Card>
    </div>
  )
}
