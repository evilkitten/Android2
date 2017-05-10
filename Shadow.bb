Const SHADOW_FILE$="Shadow"
Const SHADOW_SIZE#=1.75

Global SHADOW_MASTER

Function InitialiseShadowFiles()
	;UnPackAsset(PACK_SHADOW_ANIM_START,PACK_SHADOW_ANIM_LENGTH)
	UnPackAsset(PACK_SHADOW_MAT_START,PACK_SHADOW_MAT_LENGTH)
End Function

Function UnInitialiseShadowFiles()
	If (TEST) Then Return	
	
	;DeleteFile ShadowAnimFile()
	DeleteFile ShadowMatFile()
End Function

Function ShadowMatFile$()
	Return MatFile(SHADOW_FILE)
End Function

Function BuildShadowMaster()
	If (SHADOW_MASTER)
		;FreeEntity SHADOW_MASTER
		;SHADOW_MASTER=0
		Return
	End If
	
	InitialiseShadowFiles
	
	SHADOW_MASTER=CreateQuad()
	ScaleMesh SHADOW_MASTER,SHADOW_SIZE,SHADOW_SIZE,0.01
	RotateMesh SHADOW_MASTER,90,0,0
	EntityFX SHADOW_MASTER,9
	
	PaintShadowMaster
	
	UnInitialiseShadowFiles
	
	HideEntity SHADOW_MASTER
	PositionEntity SHADOW_MASTER,0,-150,0
End Function

Function AddShadow(Parent)
	Local X#=EntityX#(Parent,True)
	Local Z#=EntityZ#(Parent,True)
	Local Y#=GROUND_BASELINE_Y#+0.01
	
	Local Shadow=CopyEntity(SHADOW_MASTER)
	ScaleEntity Shadow,0.9,1,0.9
	PositionEntity Shadow,X,Y,Z,True
	EntityParent Shadow,Parent,True
End Function

Function PaintShadowMaster()
	Local Texture=LoadTexture(ShadowMatFile(),3+(8*SPECTRUM_MODE))
	
	If (SPECTRUM_MODE)
		SpectrumShadow(Texture)
	End If
	
	EntityTexture SHADOW_MASTER,Texture
	FreeTexture Texture
	EntityShininess SHADOW_MASTER,0.0
End Function

Function SpectrumShadow(ShadowTexture)
	Local W=TextureWidth(ShadowTexture)
	Local H=TextureHeight(ShadowTexture)
	
	Local Buffer=TextureBuffer(ShadowTexture)
	
	LockBuffer Buffer
	
	Local X
	Local Y
	
	For Y = 0 To H-1
		For X = (Y Mod 2) To W-1 Step 2
			WritePixelFast X,Y,0,Buffer
		Next
	Next
	
	UnlockBuffer Buffer
End Function
;~IDEal Editor Parameters:
;~F#5#A#11#2B#36#42
;~C#Blitz3D