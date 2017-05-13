Const PLAYER_BULLET_SPEED#=0.49
Const PLAYER_SHOOT_RANGE=3.9
Const PLAYER_BULLET_Y_OFFSET#=0.8

Global CURRENT_BULLET
Global BULLET_DISTANCE#
Global BULLET_DIRECTION
Global BULLET_GHOST.GHOST
Global PBX,PBY




Const LASER_FILE$="Laser"

Global LASER_MASTER

; Cosmetic only
Type LASER
	Field Entity
	Field SpawnFrame%
	Field Fade#
End Type

Function PlayerFire()
	LAST_PLAYER_FIRE=FRAME
	SpawnBullet()
End Function

Function DestroyBullet()
	If (CURRENT_BULLET)
		SpawnLaser()
	End If
	
	Delete BULLET_GHOST
	
	FreeEntity  CURRENT_BULLET
	CURRENT_BULLET=0
	BULLET_DISTANCE=0.0
End Function

Function SpawnLaser()
	Local L.LASER=New LASER
	L\Entity=CopyEntity(LASER_MASTER)
	
	Local Xp#=EntityX(PIVOT_PLAYER,True)
	
	Local X#=Xp-PBX
	Local Y#=EntityZ(PIVOT_PLAYER,True)-PBY
	
	;wraparound
	Select (BULLET_DIRECTION)
		Case MAP_LEFT_BOUND:
			If (X<0)
				X=X+MAPSIZEX
			End If
		Case MAP_RIGHT_BOUND:
			If (X>BULLET_DISTANCE)
				X=X-MAPSIZEX
			End If
		Default:
			;MAP_BOTTOM_BOUND, MAP_TOP_BOUND:
	End Select
	
	ScaleEntity L\Entity,(Int(Floor(X))*0.5)+0.1,0.1,(Int(Floor(Y))*0.5)+0.1,True
		
	PositionEntity L\Entity,Xp-(X*0.5),PLAYER_BULLET_Y_OFFSET,EntityZ(PIVOT_PLAYER,True)-(Y*0.5),True
	
	L\SpawnFrame=FRAME
	L\Fade=1.0
End Function

Function DestroyLaser(L.LASER)
	If (L<>Null)
		FreeEntity L\Entity
		Delete L
	End If
End Function

Function UpdateLaser()
	Local L.LASER
	For L= Each LASER
		If ((FRAME-L\SpawnFrame)>(PLAYER_FIRE_DELAY*(2-SPECTRUM_MODE))) Or (L\Fade<0.05)
			DestroyLaser(L)
		Else
			If (Not(SPECTRUM_MODE))
				L\Fade#=(L\Fade#-(TICK*0.05))
				
				TurnEntity L\Entity,(BULLET_DIRECTION>MAP_TOP_BOUND)*15,0,(BULLET_DIRECTION<MAP_LEFT_BOUND)*15
				
				EntityAlpha L\Entity,L\Fade#
			End If
			
		End If
	Next
End Function

Function InitialiseLaserFiles()
	;UnPackAsset(PACK_LASER_ANIM_START,PACK_LASER_ANIM_LENGTH)
	UnPackAsset(PACK_LASER_MAT_START,PACK_LASER_MAT_LENGTH)
End Function

Function UnInitialiseLaserFiles()
	If (TEST) Then Return
	;DeleteFile LaserAnimFile()
	DeleteFile LaserMatFile()
End Function

;Function LaserAnimFile$()
;	Return AnimFile(LASER_FILE)
;End Function

Function LaserMatFile$()
	Return MatFile(LASER_FILE)
End Function

Function BuildLaserMaster()
	If (LASER_MASTER)
		;FreeEntity LASER_MASTER
		;LASER_MASTER=0
		Return
	End If
	
	InitialiseLaserFiles
	
	LASER_MASTER=CreateCylinder(POLYGON_DENSITY,False)
	RotateMesh LASER_MASTER,90,90,0
	
	PaintLaserMaster
	SetLaserPhysics
	
	UnInitialiseLaserFiles
	
	
;	ScaleEntity LASER_MASTER,1.0,(0.5+(0.5*SPECTRUM_MODE)),1.0,True
	
	HideEntity LASER_MASTER
	PositionEntity LASER_MASTER,0,-160,0
	
End Function

Function SetLaserPhysics()
	; Do nothing
End Function

Function PaintLaserMaster()
	Local Texture=AcquireTextureMap(LaserMatFile(),6)
	;TextureBlend Texture,1
	PaintChildren(LASER_MASTER,Texture,255,0,255)
	FreeTexture Texture
	;EntityFX LASER_MASTER,49
End Function

Function SpawnBullet()
	Local X#=Int(Floor(EntityX#(PIVOT_PLAYER,True)))+0.5
	Local Y#=GROUND_BASELINE_Y#+PLAYER_BULLET_Y_OFFSET
	Local Z#=Int(Floor(EntityZ#(PIVOT_PLAYER,True)))+0.5
	
	Local Yaw=((EntityYaw(PIVOT_PLAYER,True))+180) Mod 360
	
	CURRENT_BULLET=CreateBullet()
	
	PositionEntity CURRENT_BULLET,X#,Y,Z#,True
	RotateEntity CURRENT_BULLET,0,Yaw,0,True
	
	Select ((Yaw+180) Mod 360)
		Case 0:
			BULLET_DIRECTION=MAP_BOTTOM_BOUND
		Case 90:
			BULLET_DIRECTION=MAP_RIGHT_BOUND	
		Case 180:
			BULLET_DIRECTION=MAP_TOP_BOUND
		Case 270:
			BULLET_DIRECTION=MAP_LEFT_BOUND			
	End Select
	
	GhostBullet()
	
	BULLET_DISTANCE=0.0
End Function

Function MoveBullet()
	If (CURRENT_BULLET)
		
		MoveEntity  CURRENT_BULLET,0,0,PLAYER_BULLET_SPEED#
		BULLET_DISTANCE=BULLET_DISTANCE+PLAYER_BULLET_SPEED#
		
		;Wraparound
		If (EntityX(CURRENT_BULLET,True)<0)
			PositionEntity CURRENT_BULLET,EntityX(CURRENT_BULLET,True)+MAPSIZEX,GROUND_BASELINE_Y#+PLAYER_BULLET_Y_OFFSET,EntityZ(CURRENT_BULLET,True),True
		Else
			If (EntityX(CURRENT_BULLET,True)>=MAPSIZEX)
				PositionEntity CURRENT_BULLET,EntityX(CURRENT_BULLET,True)-MAPSIZEX,GROUND_BASELINE_Y#+PLAYER_BULLET_Y_OFFSET,EntityZ(CURRENT_BULLET,True),True
			End If
		End If
		
		If (BULLET_DISTANCE>PLAYER_SHOOT_RANGE)
			DestroyBullet
		Else
			BulletCollision
		End If
	End If
End Function

Function BulletCollision()
	;Since the collision detection is severely broken. Use simple position checks for all entities. Though inefficient it's still fast enough to work
	
	Local X=Int(Floor(EntityX(CURRENT_BULLET,True)))
	Local Z=Int(Floor(EntityZ(CURRENT_BULLET,True)))
	
	PBX=X
	PBY=Z
	
	Local B.BOUNCER
	For B=Each BOUNCER
		If (Int(Floor(EntityX(B\Entity,True)))=X)
			If(Int(Floor(EntityZ(B\Entity,True)))=Z)
				DestroyBullet
				Exit
			End If
		End If
	Next
	
	If (Not(CURRENT_BULLET)) Then Return
	
	Local H.HOVERDROID
	For H=Each HOVERDROID
		If (Int(Floor(EntityX(H\Entity,True)))=X)
			If (Int(Floor(EntityZ(H\Entity,True)))=Z)
				DoHoverdroidShot(H)
				DestroyBullet
				Exit
			End If
		End If
	Next
	
	
	Local S.MILLITOIDSEGMENT
	For S=Each MILLITOIDSEGMENT
		If (Int(Floor(EntityX(S\Entity,True)))=X)
			If (Int(Floor(EntityZ(S\Entity,True)))=Z)
				DestroyBullet
				Exit
			End If
		End If
	Next
	
	If (Not(CURRENT_BULLET)) Then Return
	
	Local M.MILLITOID
	For M=Each MILLITOID
		If (Int(Floor(EntityX(M\Entity,True)))=X)
			If (Int(Floor(EntityZ(M\Entity,True)))=Z)
				DoMIllitoidShot(M)
				DestroyBullet
				Exit
			End If
		End If
	Next
	
	; Cehck for TREE collision in with WALLS
;	If (Not(CURRENT_BULLET)) Then Return
	
;	Local T.TREE	
;	For T=Each TREE
;		If (Int(Floor(EntityX(T\Entity,True)))=X)
;			If (Int(Floor(EntityZ(T\Entity,True)))=Z)
;				DestroyBullet
;				Exit
;			End If
;		End If
;	Next
	
	If (Not(CURRENT_BULLET)) Then Return
	
	If ((X=CAPSULEX) And (Z=CAPSULEY))
		DestroyBullet
	End If
	
	If (Not(CURRENT_BULLET)) Then Return
	
	Local Map=PeekArrayValue(X,Z)
	If ((Map=MAP_WALL) Or (Map=MAP_TREE))
		DestroyBullet
	End If
	
End Function

Function DoMIllitoidShot(M.MILLITOID)
	InjureMIllitoid(M)
End Function

Function DoHoverdroidShot(H.HOVERDROID)
	RemoveHoverdroid(H)
End Function
;~IDEal Editor Parameters:
;~F#12#18#1D#48#4F#61#66#70#74#8D#91#99#B5#CC#120#124
;~C#Blitz3D