/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 09.03.12
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
 <?php
function TestDataCheck()
{
var returnval;
alert();
if ( (qtytested >= 1) && (qtypassed >= 0) && (qtytested >= qtypassed))
   returnval = true;
else
   {
   alert("must enter the quantity tested and that amount or fewer for quantity passed");
   returnval = false;
   }

return returnval;
}
 ?>