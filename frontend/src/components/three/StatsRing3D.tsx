import { useRef } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import { OrbitControls } from '@react-three/drei'
import * as THREE from 'three'

interface StatsRingProps {
  stats: { totalBooks: number; totalMembers: number; activeIssues: number; overdueBooks: number }
}

function Segment({ color, innerRadius, outerRadius, startAngle, arcLength, height }: {
  color: string; innerRadius: number; outerRadius: number; startAngle: number; arcLength: number; height: number
}) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.position.y = Math.sin(state.clock.elapsedTime * 0.8 + startAngle) * 0.05
    }
  })

  const shape = new THREE.Shape()
  const segments = 32
  // Outer arc
  for (let i = 0; i <= segments; i++) {
    const angle = startAngle + (i / segments) * arcLength
    const x = Math.cos(angle) * outerRadius
    const y = Math.sin(angle) * outerRadius
    if (i === 0) shape.moveTo(x, y)
    else shape.lineTo(x, y)
  }
  // Inner arc (reverse)
  for (let i = segments; i >= 0; i--) {
    const angle = startAngle + (i / segments) * arcLength
    const x = Math.cos(angle) * innerRadius
    const y = Math.sin(angle) * innerRadius
    shape.lineTo(x, y)
  }
  shape.closePath()

  return (
    <mesh ref={meshRef} rotation={[-Math.PI / 2, 0, 0]}>
      <extrudeGeometry args={[shape, { depth: height, bevelEnabled: true, bevelThickness: 0.04, bevelSize: 0.04, bevelSegments: 2 }]} />
      <meshStandardMaterial color={color} roughness={0.35} metalness={0.25} />
    </mesh>
  )
}

function Chart({ stats }: StatsRingProps) {
  const groupRef = useRef<THREE.Group>(null)

  useFrame((_, delta) => {
    if (groupRef.current) {
      groupRef.current.rotation.y += delta * 0.2
    }
  })

  const total = stats.totalBooks + stats.totalMembers + stats.activeIssues + stats.overdueBooks
  if (total === 0) return null

  const data = [
    { value: stats.totalBooks, color: '#0d9488' },
    { value: stats.totalMembers, color: '#16a34a' },
    { value: stats.activeIssues, color: '#d97706' },
    { value: stats.overdueBooks, color: '#dc2626' },
  ]

  let angle = 0
  const arcs = data.map((d) => {
    const arc = (d.value / total) * Math.PI * 2
    const seg = { ...d, startAngle: angle, arcLength: arc - 0.04 }
    angle += arc
    return seg
  })

  return (
    <group ref={groupRef}>
      {arcs.map((arc, i) => (
        <Segment key={i} innerRadius={1} outerRadius={1.8} startAngle={arc.startAngle} arcLength={Math.max(0.01, arc.arcLength)} height={0.4} color={arc.color} />
      ))}
    </group>
  )
}

export function StatsRing3D({ stats }: StatsRingProps) {
  return (
    <div className="w-full h-56 rounded-lg">
      <Canvas camera={{ position: [0, 3, 3.5], fov: 45 }}>
        <ambientLight intensity={0.5} />
        <directionalLight position={[3, 5, 4]} intensity={0.6} />
        <Chart stats={stats} />
        <OrbitControls enablePan={false} enableZoom={false} />
      </Canvas>
    </div>
  )
}
