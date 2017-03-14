;Const MILLITOID_SEGMENTS_MAX=4 - SHARED with MAP Editor

Const MILLITOID_HEAD_FILE$="Millitoid_Head"
Const MILLITOID_SEGMENT_FILE$="Millitoid_Segment"

Type MILLITOID
	Field MillitoidNumber
	Field Entity
	Field Segment[MILLITOID_SEGMENTS_MAX];(Handle(MILLITOIDSEGMENT))
	Field Segments
End Type

Type MILLITOIDSEGMENT
	Field Parent.MILLITOID
	Field TargetPos#[1]
	Field Segment
	Field Entity
End Type

Global MILLITOID_HEAD_MASTER
Global MILLITOID_SEGMENT_MASTER
Global MILLITOID_COLLISION_RADIUS#

Global MILLITOID_HEAD_Y_OFFSET#
Global MILLITOID_SEGMENT_Y_OFFSET#

Global MILLITOID_REMAINING

Function MillitoidHeadAnimFile$()
	Return AnimFile(MILLITOID_HEAD_FILE)
End Function

Function MillitoidSegmentAnimFile$()
	Return AnimFile(MILLITOID_SEGMENT_FILE)
End Function

Function MillitoidHeadMatFile$()
	Return MatFile(MILLITOID_HEAD_FILE)
End Function

Function MillitoidSegmentMatFile$()
	Return MatFile(MILLITOID_SEGMENT_FILE)
End Function

Function BuildMillitoidHeadMaster()
	If (MILLITOID_HEAD_MASTER)
		FreeEntity MILLITOID_HEAD_MASTER
		MILLITOID_HEAD_MASTER=0
	End If
	
	If (SPECTRUM_MODE)
		MILLITOID_HEAD_MASTER=CreateSphere(POLYGON_DENSITY)
	Else
		MILLITOID_HEAD_MASTER=LoadAnimMesh(MillitoidHeadAnimFile())
	End If
	
	InitialiseMillitoidHeadAnimSequences
	PaintMillitoidHead
	SetMillitoidHeadPhysics
	ScaleEntity MILLITOID_HEAD_MASTER,0.75-(0.35*SPECTRUM_MODE),0.25+(0.15*SPECTRUM_MODE),0.5,True
	
	HideEntity MILLITOID_HEAD_MASTER
	PositionEntity MILLITOID_HEAD_MASTER,0,-110,0
End Function

Function PaintMillitoidHead()
	Local Texture=AcquireTextureMap(MillitoidHeadMatFile())
		PaintChildren(MILLITOID_HEAD_MASTER,Texture,255,0,0)
		FreeTexture Texture
		EntityShininess MILLITOID_HEAD_MASTER,0.25+(SPECTRUM_MODE*0.75)
End Function

Function SetMillitoidHeadPhysics()
	;EntityPickMode MILLITOID_HEAD_MASTER,3,True
	
	MILLITOID_COLLISION_RADIUS=0.25
	MILLITOID_HEAD_Y_OFFSET#=0.65-(0.4*SPECTRUM_MODE)
End Function

Function BuildMillitoidSegmentMaster()
	If (MILLITOID_SEGMENT_MASTER)
		FreeEntity MILLITOID_SEGMENT_MASTER
		MILLITOID_SEGMENT_MASTER=0
	End If
	
	If SPECTRUM_MODE 
		MILLITOID_SEGMENT_MASTER=CreateCube()
	Else
		MILLITOID_SEGMENT_MASTER=LoadAnimMesh(MillitoidSegmentAnimFile())
	End If
	
	InitialiseMillitoidSegmentAnimSequences
	
	PaintMillitoidSegment
	SetMillitoidSegmentPhysics
	ScaleEntity MILLITOID_SEGMENT_MASTER,0.333,0.25+(0.25*SPECTRUM_MODE),0.6,True
	
	HideEntity MILLITOID_SEGMENT_MASTER
	PositionEntity MILLITOID_SEGMENT_MASTER,0,-120,0
End Function

Function PaintMillitoidSegment()
	Local Texture=AcquireTextureMap(MillitoidSegmentMatFile())
	PaintChildren(MILLITOID_SEGMENT_MASTER,Texture,0,0,255)
	FreeTexture Texture
	
	EntityShininess MILLITOID_SEGMENT_MASTER,0.2
End Function

Function SetMillitoidSegmentPhysics()
	;EntityPickMode MILLITOID_SEGMENT_MASTER,2,True
	MILLITOID_SEGMENT_Y_OFFSET#=0.15
End Function

Function SetMillitoidAnimation(M.MILLITOID)
	SetMillitoidHeadAnimation(M\Entity)
	Local Iter
	For Iter=1 To M\Segments
		Local S.MILLITOIDSEGMENT=Object.MILLITOIDSEGMENT(M\Segment[Iter-1])
		SetMillitoidSegmentAnimation(S\Entity)
	Next
End Function

Function InitialiseMillitoidHeadAnimSequences()
	LoadAnimSeq(MILLITOID_HEAD_MASTER,MillitoidHeadAnimFile$())
End Function

Function InitialiseMillitoidSegmentAnimSequences()
	LoadAnimSeq(MILLITOID_SEGMENT_MASTER,MillitoidSegmentAnimFile())
End Function

Function SetMillitoidHeadAnimation(Entity)
	If Not(SPECTRUM_MODE) Then Animate Entity,1,0.1,1.0,1.0
End Function

Function SetMillitoidSegmentAnimation(Entity)
	If Not(SPECTRUM_MODE) Then Animate Entity,1,Rnd(0.05,0.1),1.0,1.0
End Function

Function MoveMillitoid(M.MILLITOID)
	Local X#=EntityX(M\Entity,True)
	Local Z#=EntityZ(M\Entity,True)
	
	If (X#>MAPSIZEX) Or (X#<0)
		Local WrapX=(X#+MAPSIZEX)Mod MAPSIZEX
		Local RelativeX#=(WrapX-X)
		
		PositionEntity M\Entity,WrapX,GROUND_BASELINE_Y#+MILLITOID_HEAD_Y_OFFSET,Z,True
		WrapAroundSegments(M,RelativeX#)
		Return
	End If
	
	Local Picked=EntityPick(M\Entity,1.0)
	
	If (Picked)
		Local Rotate=Rnd(0,1)
		Rotate=((-1*(Not(Rotate)))+(1*Rotate))
		TurnEntity M\Entity,0,Rotate*90,0,True
	Else
		MoveEntity M\Entity,0,0,0.1*TICK
		SegmentsFollow(M)
		Return
	End If
	
End Function

Function SegmentsFollow(M.MILLITOID)
	Local Iter
	For Iter=1 To M\Segments
		Local Shandle=M\Segment[Iter-1]
		Local S.MILLITOIDSEGMENT=Object.MILLITOIDSEGMENT(Shandle)
		MoveSegment(S)
	Next
End Function

Function WrapAroundSegments(M.MILLITOID,RelativeX)
	Local Iter
	For Iter=1 To M\Segments
		Local Shandle=M\Segment[Iter-1]
		Local S.MILLITOIDSEGMENT=Object.MILLITOIDSEGMENT(Shandle)
		Local X#=EntityX#(S\Entity,True)
		Local Z#=EntityZ#(S\Entity,True)
		
		X#=X+RelativeX
		S\TargetPos[0]=S\TargetPos[0]+RelativeX
		PositionEntity S\Entity,X#,GROUND_BASELINE_Y#+MILLITOID_SEGMENT_Y_OFFSET,Z#,True
	Next
End Function

Function SpawnMillitoid(X#,Z#,D=MAP_TOP_BOUND)
	MILLITOID_REMAINING=MILLITOID_REMAINING+1
	
	Local M.MILLITOID=New MILLITOID
	M\MillitoidNumber=MILLITOID_REMAINING
	
	M\Entity=CopyEntity(MILLITOID_HEAD_MASTER)
	
	M\Segments=MILLITOID_SEGMENTS_MAX+1
	PositionEntity M\Entity,X#,GROUND_BASELINE_Y#+MILLITOID_HEAD_Y_OFFSET,Z#,True
	
	Local XX=0
	Local ZZ=0
	Local Yaw#
	
	Select D
		Case MAP_BOTTOM_BOUND:
			Yaw#=180.0
			ZZ=1
		Case MAP_TOP_BOUND:
			Yaw#=0.0
			ZZ=-1
		Case MAP_LEFT_BOUND:
			Yaw#=90.0
			XX=1
		Case MAP_RIGHT_BOUND:
			Yaw#=270.0
			XX=-1
	End Select
	Local Iter
	
	RotateEntity M\Entity,0,Yaw#,0,True
	Local PrevEnt=M\Entity
	Local S.MILLITOIDSEGMENT
	
	For Iter=0 To MILLITOID_SEGMENTS_MAX
		X#=X#+XX
		Z#=Z#+ZZ
		S=AddSegment(M,PrevEnt,Iter+1,X#,Z#,Yaw#)
		M\Segment[Iter]=Handle(S)
		PrevEnt=S\Entity
		AddShadow(S\Entity)
	Next
	
	AddShadow M\Entity
	
	UpdateWorld
	
	SetMillitoidAnimation(M)
	GhostMillitoid(M)
End Function

Function AddSegment.MILLITOIDSEGMENT(M.MILLITOID,PreviousSegmentEntity,Segment,X#,Z#,Yaw#)
	Local S.MILLITOIDSEGMENT=New MILLITOIDSEGMENT
	S\Parent=M
	S\Segment=Segment
	S\Entity=CopyEntity(MILLITOID_SEGMENT_MASTER)
	S\TargetPos[0]=EntityX(PreviousSegmentEntity,True)
	S\TargetPos[1]=EntityZ(PreviousSegmentEntity,True)
	PositionEntity S\Entity,X#,GROUND_BASELINE_Y#+MILLITOID_SEGMENT_Y_OFFSET,Z#,True
	RotateEntity S\Entity,0,Yaw#,0,True
	
	Return S
End Function

Function MoveSegment(S.MILLITOIDSEGMENT)
	;Calculate position relative to target position
	Local X#=EntityX#(S\Entity,True)
	Local TX#=(S\TargetPos[0]-X#)
	Local Z#=EntityZ#(S\Entity,True)
	Local TZ#=(S\TargetPos[1]-Z#)
	
	;Only set a new target position when close enough to the target position
	If ((Sqr(TX*TX)+(TZ*TZ))<0.2)
		Local Parent
		
		If (S\Segment=1)
			;This is first segment, use Head position as target
			Parent=S\Parent\Entity
		Else
			;Use preceeding segment position as target
			Local P.MILLITOIDSEGMENT=Object.MILLITOIDSEGMENT(S\Parent\Segment[S\Segment-2])
			Parent=P\Entity
		End If
		
		TX=EntityX#(Parent,True)
		TZ=EntityZ#(Parent,True)
		
		;Fix in case too close to preceeding segment
		
		If (EntityDistance(S\Entity,Parent)>0.9)
			;Update target position
			S\TargetPos[0]=TX
			S\TargetPos[1]=TZ
		End If
	End If
	
	;Use TX and TZ to represent target coords for simplicity
	TX#=S\TargetPos[0]
	TZ#=S\TargetPos[1]
	
	;Ensure pointing towards target and move forwards
	Local Yaw#=Angle2D(TX,TZ,X,Z)
	RotateEntity S\Entity,0,Yaw#,0,True
	MoveEntity S\Entity,0,0,0.1*TICK
End Function	

;~IDEal Editor Parameters:
;~F#5#C#1C#20#24#28#2C#41#48#4F#65#6D#72#7B#7F#83#87#8B#A6#AF
;~F#BD#F1#FE
;~C#Blitz3D