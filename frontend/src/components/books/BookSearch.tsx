import { Search } from 'lucide-react'
import { Input } from '@/components/ui/input'

interface BookSearchProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

export function BookSearch({ value, onChange, placeholder = 'Search books...' }: BookSearchProps) {
  return (
    <div className="relative">
      <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-stone-400" />
      <Input
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        className="pl-9"
      />
    </div>
  )
}
