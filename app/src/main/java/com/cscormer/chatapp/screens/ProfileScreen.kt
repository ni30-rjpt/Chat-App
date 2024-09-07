package com.cscormer.chatapp.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cscormer.chatapp.CAViewModel
import com.cscormer.chatapp.DestinationScreen
import com.cscormer.chatapp.ui.theme.CommanDivider
import com.cscormer.chatapp.ui.theme.CommonImage
import com.cscormer.chatapp.ui.theme.CommonProgressBar
import com.cscormer.chatapp.ui.theme.navigateTo

@Composable
fun ProfileScreen(navController: NavController, vm: CAViewModel) {
    val inProgress =vm.inProcess.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        val userData= vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name?:"")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number?:"")
        }
        Column {

            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                vm= vm,
                name = name,
                number =number,
                onNameChange = {name=it},
                onNumberChange = {number=it},
                onSave = {
                         vm.createOrUpdateProfile(name= name, number= number)
                },
                onBack = {
                         navigateTo(navController= navController, route = DestinationScreen.ChatList.route)
                },
                onLogout = {
                    vm.logout()
                    navigateTo(navController= navController, route = DestinationScreen.Login.route)
                }
            )
            CommanDivider()
            BootomNevigationManu(
                selectedItem = BootomNevigationItem.PROFILE,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
 @Composable
fun ProfileContent(
    modifier: Modifier,
    vm: CAViewModel,
    name: String,
    number: String,
    onNameChange:(String)->Unit,
    onNumberChange:(String)->Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogout:()->Unit
) {
    val imageUrl = vm.userData.value?.imageUrl


    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", modifier= Modifier.clickable { onBack.invoke() })
           Text(text = "Save", modifier= Modifier.clickable { onSave.invoke() })
        }
            CommanDivider()
            ProfileImage(imageUrl = imageUrl, vm = vm)

            CommanDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Name", modifier = Modifier.width(100.dp))
                TextField(
                    value = name, onValueChange = onNameChange,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )

                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Number", modifier = Modifier.width(100.dp))
                TextField(
                    value = number, onValueChange = onNumberChange,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )

                )
            }
            CommanDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Log Out", modifier = Modifier.clickable { onLogout.invoke() })
            }
        }
    }

@Composable
fun ProfileImage(imageUrl: String?, vm: CAViewModel) {
    val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                vm.uploadProfileImage(uri)
            }
        }

    Box(modifier = Modifier.height(intrinsicSize =IntrinsicSize.Min))
    {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            )
            { CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Picture")
        }
        val isLoading = vm.inProcess.value
       if (isLoading)
            CommonProgressBar()

    }
}