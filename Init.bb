Global TEMP_DIR$

Const PUBLISHER$="EKD"
Const APP_TITLE$="Android 2"

Const MESH_EXTENSION$=".3ds"

Const ANIM_EXTENSION$="_Anim"
Const MAT_EXTENSION$="_Mat"

Function Initialise()
	InitialiseCommandLineParameters()
	InitialiseAssets
	InitialiseDefaultControls
	RUNTIME_Example
End Function

Function InitialiseCommandLineParameters()
	Local CLP$=Lower(CommandLine())
	SPECTRUM_MODE=Instr(CLP,"/zx")
	WIRE=Instr(CLP,"/wire")
End Function

Function InitialiseFadeMesh()
	If (CAM_FADE_MESH)
		FreeEntity CAM_FADE_MESH
		CAM_FADE_MESH=0
	End If
	
	CAM_FADE_MESH=SpawnFadeMesh()
	
	CAM_FADE=CAM_FADE_STATE_TRANSP
	CAM_FADE_VALUE#=0.0
	
	EntityParent CAM_FADE_MESH,CAM,True
	
End Function

Function SpawnFadeMesh()
	Local Quad
	Quad=CreateQuad(CAM_PIVOT)
	RotateMesh Quad,90,0,0
	
	If (SPECTRUM_MODE)
		ScaleMesh Quad,24,0.1,27.2
		TranslateEntity Quad,0,GROUND_BASELINE_Y+3,-0.05,True
	Else
		ScaleMesh Quad,3,0.1,3
		TranslateEntity Quad,0,CAM_LIMIT-1.5,1,True
	End If
	
	EntityFX Quad,1
	HideEntity Quad
	
	Return Quad
End Function
		
Function AcquireTextureMap(Filepath$,WithFLag=0)
	Return LoadTexture(Filepath,257+WithFLag)
End Function

Function AcquireAnimTextureMap(FilePath$)
	Return LoadAnimTexture(FilePath$,257,256,256,0,2)
End Function

Function MatFile$(Value$)
	;Exchange for packesd or direct use of resource - Remember to change back to packed for release
	
	If (TEST) Then Return VisualDir()+"Assets\"+Value+MAT_EXTENSION+IMG_EXTENSION
	Return TempDir()+Value+MAT_EXTENSION+IMG_EXTENSION
End Function

Function AnimFile$(Value$)
	;Exchange for packesd or direct use of resource - Remember to change back to packed for release
	If (TEST) Then Return VisualDir()+"Assets\"+Value+ANIM_EXTENSION+MESH_EXTENSION
	Return TempDir()+Value+ANIM_EXTENSION+MESH_EXTENSION
End Function

Function InitialiseTempDir()
	TEMP_DIR=GetEnv("TMP")+"\"+PUBLISHER+"\"
	
	If (FileType(TEMP_DIR)<>2)
		CreateDir TEMP_DIR
	End If
	
	If (FileType(TEMP_DIR)<>2)
		RuntimeError(TEMP_DIR+" could not be created.")
	End If
	
	TEMP_DIR$=TEMP_DIR$+APP_TITLE$+"\"
	
	If (FileType(TEMP_DIR)<>2)
		CreateDir TEMP_DIR
	End If
	
	If (FileType(TEMP_DIR)<>2)
		RuntimeError(TEMP_DIR+" could not be created.")
	End If
End Function

Function TempDir$()
	Return TEMP_DIR$
End Function

Function InitialiseAssets()
	InitialiseSharedFilesystem
	InitialiseTempDir
End Function

Function InitialiseGameMasters()
	;Initialises all except for PLAYER . Player is initialised AFTER map load. PLAYER is the Master.
	BuildShadowMaster
	
	BuildTreeMaster
	
	BuildMineMaster
	
	BuildBouncerMaster
	
	BuildHoverdroidMaster
	
	BuildMillitoidMaster
	
	BuildLaserMaster
End Function

;~IDEal Editor Parameters:
;~F#A#11#17#26#39#3D#41#48#4E#64#68#6D
;~C#Blitz3D