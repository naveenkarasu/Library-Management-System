import { Menu, LogOut, User } from 'lucide-react'
import { useAuthStore } from '@/hooks/useAuth'
import { Button } from '@/components/ui/button'

interface HeaderProps {
  onMenuClick: () => void;
}

export function Header({ onMenuClick }: HeaderProps) {
  const { user, logout } = useAuthStore()

  return (
    <header className="sticky top-0 z-30 h-16 bg-white border-b border-teal-100 flex items-center justify-between px-4 lg:px-6">
      <button
        className="lg:hidden p-2 rounded-md text-teal-700 hover:bg-teal-50"
        onClick={onMenuClick}
      >
        <Menu className="h-5 w-5" />
      </button>

      <div className="flex-1" />

      <div className="flex items-center gap-4">
        {user && (
          <div className="flex items-center gap-2 text-sm text-teal-800">
            <User className="h-4 w-4" />
            <span className="hidden sm:inline">{user.username}</span>
            <span className="hidden sm:inline text-xs bg-teal-100 text-teal-700 px-2 py-0.5 rounded-full">
              {user.role}
            </span>
          </div>
        )}
        <Button variant="ghost" size="sm" onClick={logout} className="text-teal-700 hover:text-teal-900">
          <LogOut className="h-4 w-4 mr-1" />
          <span className="hidden sm:inline">Logout</span>
        </Button>
      </div>
    </header>
  )
}
